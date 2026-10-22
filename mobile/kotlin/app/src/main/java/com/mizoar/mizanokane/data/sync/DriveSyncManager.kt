package com.mizoar.mizanokane.data.sync

import android.accounts.Account
import android.app.Activity
import android.app.PendingIntent
import android.util.Log
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.auth.AuthPreferences
import com.mizoar.mizanokane.data.auth.GoogleAuthConfig
import com.mizoar.mizanokane.data.json.BackupRootDto
import com.mizoar.mizanokane.data.json.BudgetDto
import com.mizoar.mizanokane.data.json.JsonBackupManager
import com.mizoar.mizanokane.data.json.SettingsDto
import com.mizoar.mizanokane.data.json.TransactionDto
import com.mizoar.mizanokane.data.json.WalletDto
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.CategoryItem
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.SubcategoriesGroup
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import com.mizoar.mizanokane.data.repository.MizanRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Hasil dari eksekusi sinkronisasi Google Drive.
 */
data class SyncResult(
    val success: Boolean,
    val lastSyncAt: Long,
    val transactionsCount: Int
)

/**
 * Pengelola siklus sinkronisasi Google Drive aplikasi Mizan Okane (Kotlin 8B).
 *
 * Mengikuti aturan dev/PLANSPEC.md dan web (desktop/src/js/sync.js):
 * - Mengambil file dari appDataFolder Google Drive (jika ada).
 * - Menggabungkan data lokal dan data remote dengan resolusi konflik Last-Write-Wins (updatedAt).
 * - Menyimpan hasil gabungan ke Room database lokal.
 * - Mengunggah hasil gabungan ke Drive.
 * - Memperbarui waktu sinkron terakhir di AuthPreferences.
 * - Token disimpan HANYA di memori, tidak pernah ke storage.
 */
object DriveSyncManager {

    private var inMemoryToken: String? = null
    private var tokenExpiresAt: Long = 0L

    fun setMemoryToken(token: String, expiresInSeconds: Long = 3600L) {
        inMemoryToken = token
        tokenExpiresAt = System.currentTimeMillis() + (expiresInSeconds * 1000L)
    }

    fun clearMemoryToken() {
        inMemoryToken = null
        tokenExpiresAt = 0L
    }

    fun isTokenValid(): Boolean {
        return inMemoryToken != null && System.currentTimeMillis() < (tokenExpiresAt - 60_000L)
    }

    fun getValidToken(): String? {
        return if (isTokenValid()) inMemoryToken else null
    }

    /**
     * Meminta access token ke Google Play Services Identity AuthorizationClient.
     * Jika token di memori masih valid, langsung dikembalikan.
     * Jika izin butuh resolusi interaktif pengguna (consent dialog), memanggil [onResolutionRequired]
     * dan mengembalikan null.
     * Jika izin sudah pernah disetujui sebelumnya, token dikembalikan secara senyap (silent).
     */
    suspend fun getOrRequestAccessToken(
        activity: Activity,
        email: String,
        onResolutionRequired: (PendingIntent) -> Unit
    ): String? {
        val cached = getValidToken()
        if (cached != null) return cached

        val requestedScopes = listOf(Scope(GoogleAuthConfig.DRIVE_APPDATA_SCOPE))
        val authRequest = AuthorizationRequest.builder()
            .setRequestedScopes(requestedScopes)
            .setAccount(Account(email, "com.google"))
            .build()

        val authResult = Identity.getAuthorizationClient(activity)
            .authorize(authRequest)
            .awaitTask()

        if (authResult.hasResolution()) {
            val pendingIntent = authResult.pendingIntent
                ?: throw IllegalStateException("PendingIntent resolusi otorisasi null")
            onResolutionRequired(pendingIntent)
            return null
        } else {
            val token = authResult.accessToken
                ?: throw IllegalStateException("Token otorisasi Drive tidak diterima")
            setMemoryToken(token)
            return token
        }
    }

    /**
     * Menjalankan satu kali alur sinkronisasi data:
     * 1. Ambil file dari Drive (kalau belum ada, anggap kosong).
     * 2. Gabungkan dengan data lokal (aturan: per ID, waktu ubah terbaru menang; soft delete ikut digabung).
     * 3. Simpan hasil gabungan ke Room.
     * 4. Unggah hasil gabungan ke Drive.
     * 5. Catat waktu sinkron terakhir di AuthPreferences.
     */
    suspend fun performSync(
        accessToken: String,
        repository: MizanRepository,
        authPreferences: AuthPreferences
    ): SyncResult = withContext(Dispatchers.IO) {
        // 1. Ambil file dari Drive (jika belum ada, remoteData = null)
        val remoteFileMeta = DriveApiClient.findRemoteFile(accessToken)

        val remoteData: BackupRootDto? = if (remoteFileMeta != null) {
            val jsonText = DriveApiClient.downloadRemote(remoteFileMeta.id, accessToken)
            parseRemoteSyncData(jsonText)
        } else {
            null
        }

        // 2. Ambil data lokal
        val localRealTransactions = repository.getRealTransactions().filter { !it.dummy }
        val allLocalTransactions = repository.getAllTransactions()
        val localDummyTransactions = allLocalTransactions.filter { it.dummy }
        val localWallets = repository.getRealWallets()
        val localCategories = repository.getRealCategories()
        val localBudgets = repository.getRealBudgets()
        val localSettings = repository.getSettings()

        // 3. Gabungkan data (per ID, terbaru menang)
        val mergedTransactions: List<TransactionEntity>
        val finalCategories: CategoriesData
        val finalBudgets: BudgetsData
        val finalWallets: List<WalletEntity>
        val finalSettings: SettingsEntity

        if (remoteData != null) {
            // 3a. Transaksi: Last-Write-Wins berdasarkan updatedAt (atau createdAt)
            val txMap = LinkedHashMap<String, TransactionEntity>()
            for (dto in remoteData.transactions) {
                if (!dto.dummy && JsonBackupManager.isValidTransaction(dto)) {
                    txMap[dto.id] = dto.toEntity()
                }
            }
            for (localTx in localRealTransactions) {
                val existing = txMap[localTx.id]
                if (existing == null) {
                    txMap[localTx.id] = localTx
                } else {
                    val localTime = if (localTx.updatedAt > 0L) localTx.updatedAt else localTx.createdAt
                    val existTime = if (existing.updatedAt > 0L) existing.updatedAt else existing.createdAt
                    if (localTime >= existTime) {
                        txMap[localTx.id] = localTx
                    }
                }
            }
            mergedTransactions = txMap.values.toList()

            // 3b. Kategori
            val remoteCats = remoteData.categories
            val remoteCatTime = remoteCats?.updatedAt ?: 0L
            val localCatTime = localCategories.updatedAt
            finalCategories = if (remoteCats != null && remoteCatTime > localCatTime) {
                JsonBackupManager.sanitizeCategories(remoteCats)
            } else {
                localCategories
            }

            // 3c. Budget
            val remoteBudgetsDto = remoteData.budgets
            val remoteBudgetsData = remoteBudgetsDto?.toBudgetsData()
            val remoteBudgetTime = remoteBudgetsData?.updatedAt ?: 0L
            val localBudgetTime = localBudgets.updatedAt
            finalBudgets = if (remoteBudgetsData != null && remoteBudgetTime > localBudgetTime) {
                remoteBudgetsData
            } else {
                localBudgets
            }

            // 3d. Dompet
            val remoteWallets = remoteData.wallets.map { it.toEntity() }
            finalWallets = if (remoteWallets.isNotEmpty()) {
                JsonBackupManager.mergeWalletsList(localWallets, remoteWallets)
            } else {
                localWallets
            }

            // 3e. Pengaturan
            val remoteSettings = remoteData.settings?.toEntity()
            val remoteSettingsTime = remoteSettings?.updatedAt ?: 0L
            val localSettingsTime = localSettings.updatedAt
            finalSettings = if (remoteSettings != null && remoteSettingsTime > localSettingsTime) {
                remoteSettings
            } else {
                localSettings
            }
        } else {
            // Belum ada file di Drive: data lokal menjadi acuan awal
            mergedTransactions = localRealTransactions
            finalCategories = localCategories
            finalBudgets = localBudgets
            finalWallets = localWallets
            finalSettings = localSettings
        }

        // 4. Simpan hasil gabungan ke Room database lokal
        repository.replaceAllTransactions(mergedTransactions + localDummyTransactions)
        repository.saveCategories(finalCategories)
        repository.saveBudgets(finalBudgets)
        repository.replaceAllWallets(finalWallets)
        repository.saveSettings(finalSettings)

        // 5. Susun payload berkas cadangan resmi untuk diunggah ke Google Drive
        val payloadDto = BackupRootDto(
            app = "mizan-okane",
            version = 2,
            schemaVersion = 2,
            exportedAt = Instant.now().toString(),
            transactions = mergedTransactions.map { TransactionDto.fromEntity(it) },
            wallets = finalWallets.map { WalletDto.fromEntity(it) },
            categories = finalCategories,
            budgets = BudgetDto.fromBudgetsData(finalBudgets),
            settings = SettingsDto.fromEntity(finalSettings)
        )
        val jsonString = JsonBackupManager.json.encodeToString(BackupRootDto.serializer(), payloadDto)

        // 6. Unggah hasil gabungan ke Google Drive
        DriveApiClient.uploadRemote(
            fileId = remoteFileMeta?.id,
            jsonContent = jsonString,
            accessToken = accessToken
        )

        // 7. Catat waktu sinkron terakhir setelah unggah berhasil
        val now = System.currentTimeMillis()
        authPreferences.setLastSyncAt(now)

        SyncResult(
            success = true,
            lastSyncAt = now,
            transactionsCount = mergedTransactions.size
        )
    }

    /**
     * Membaca dan memvalidasi isi berkas sinkronisasi dari Google Drive.
     * Menerima persis format yang ditulis oleh versi web (desktop/src/js/sync.js).
     *
     * Format dianggap dikenali jika:
     * 1. Berkas berupa JSON Object yang valid.
     * 2. Memiliki field "transactions" berupa array.
     * 3. Setiap elemen transaksi berupa object dengan field "id" string yang valid.
     *
     * Jika format tidak dikenali, mencatat ke Logcat tag "MizanSync" daftar kunci tingkat atas
     * dan nilai field versi tanpa mencatat data keuangan, lalu melempar [IllegalStateException].
     */
    fun parseRemoteSyncData(jsonText: String): BackupRootDto {
        val rootElement: JsonElement = try {
            JsonBackupManager.json.parseToJsonElement(jsonText)
        } catch (e: Exception) {
            Log.w("MizanSync", "Berkas sinkron di Drive rusak (bukan JSON): ${e.message}")
            throw IllegalStateException("Format file sinkron di Drive tidak dikenali.", e)
        }

        val rootObject = rootElement as? JsonObject
        val rawTransactions = rootObject?.get("transactions") as? JsonArray

        val isValidFormat = rootObject != null &&
            rawTransactions != null &&
            rawTransactions.all { elem ->
                if (elem !is JsonObject) return@all false
                val idElem = elem["id"] as? JsonPrimitive
                idElem != null && idElem.isString && idElem.content.isNotBlank()
            }

        if (!isValidFormat) {
            val topLevelKeys = rootObject?.keys?.toList() ?: emptyList()
            val versionVal = (rootObject?.get("version") as? JsonPrimitive)?.contentOrNull
            val schemaVersionVal = (rootObject?.get("schemaVersion") as? JsonPrimitive)?.contentOrNull
            Log.w(
                "MizanSync",
                "Format file sinkron di Drive tidak dikenali. Kunci tingkat atas: $topLevelKeys, version: $versionVal, schemaVersion: $schemaVersionVal"
            )
            throw IllegalStateException("Format file sinkron di Drive tidak dikenali.")
        }

        val app = (rootObject["app"] as? JsonPrimitive)?.contentOrNull ?: "mizan-okane"
        val version = (rootObject["version"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull()
            ?: (rootObject["schemaVersion"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull()
            ?: 2
        val schemaVersion = (rootObject["schemaVersion"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull()
            ?: (rootObject["version"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull()
            ?: 2
        val exportedAt = (rootObject["exportedAt"] as? JsonPrimitive)?.contentOrNull ?: ""

        // 1. Transaksi
        val parsedTransactions = ArrayList<TransactionDto>()
        for (elem in rawTransactions) {
            if (elem is JsonObject) {
                val dto = try {
                    JsonBackupManager.json.decodeFromJsonElement<TransactionDto>(elem)
                } catch (_: Exception) {
                    parseTransactionElementSafely(elem)
                }
                if (dto != null) {
                    parsedTransactions.add(dto)
                }
            }
        }

        // 2. Kategori
        val rawCategories = rootObject["categories"] as? JsonObject
        val categoriesData: CategoriesData? = if (rawCategories != null) {
            try {
                JsonBackupManager.json.decodeFromJsonElement<CategoriesData>(rawCategories)
            } catch (_: Exception) {
                parseCategoriesSafely(rawCategories)
            }
        } else {
            null
        }

        // 3. Budget
        val rawBudgets = rootObject["budgets"] as? JsonObject
        val budgetDto: BudgetDto? = if (rawBudgets != null) {
            try {
                JsonBackupManager.json.decodeFromJsonElement<BudgetDto>(rawBudgets)
            } catch (_: Exception) {
                parseBudgetsSafely(rawBudgets)
            }
        } else {
            null
        }

        // 4. Dompet
        val rawWallets = rootObject["wallets"] as? JsonArray
        val walletList: List<WalletDto> = if (rawWallets != null) {
            parseWalletsSafely(rawWallets)
        } else {
            emptyList()
        }

        // 5. Pengaturan
        val rawSettings = rootObject["settings"] as? JsonObject
        val settingsDto: SettingsDto? = if (rawSettings != null) {
            try {
                JsonBackupManager.json.decodeFromJsonElement<SettingsDto>(rawSettings)
            } catch (_: Exception) {
                val theme = (rawSettings["theme"] as? JsonPrimitive)?.contentOrNull?.trim() ?: "system"
                val timeFormat = (rawSettings["timeFormat"] as? JsonPrimitive)?.contentOrNull?.trim() ?: "24"
                val updatedAt = (rawSettings["updatedAt"] as? JsonPrimitive)?.contentOrNull?.toLongOrNull() ?: 0L
                SettingsDto(theme = theme, timeFormat = timeFormat, updatedAt = updatedAt)
            }
        } else {
            null
        }

        return BackupRootDto(
            app = app,
            version = version,
            schemaVersion = schemaVersion,
            exportedAt = exportedAt,
            transactions = parsedTransactions,
            wallets = walletList,
            categories = categoriesData,
            budgets = budgetDto,
            settings = settingsDto
        )
    }

    private fun parseTransactionElementSafely(obj: JsonObject): TransactionDto? {
        val id = (obj["id"] as? JsonPrimitive)?.contentOrNull?.trim() ?: return null
        if (id.isEmpty()) return null
        val type = (obj["type"] as? JsonPrimitive)?.contentOrNull?.trim() ?: return null
        val amountElem = obj["amount"] as? JsonPrimitive ?: return null
        val amount = try {
            Uang.dariJsonPrimitive(amountElem)
        } catch (_: Exception) {
            val amtDouble = amountElem.content.toDoubleOrNull() ?: return null
            BigDecimal.valueOf(amtDouble).multiply(BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).longValueExact()
        }
        val date = (obj["date"] as? JsonPrimitive)?.contentOrNull?.trim() ?: return null
        val time = (obj["time"] as? JsonPrimitive)?.contentOrNull?.trim()
        val category = (obj["category"] as? JsonPrimitive)?.contentOrNull?.trim()
        val subcategory = (obj["subcategory"] as? JsonPrimitive)?.contentOrNull?.trim()
        val walletId = (obj["walletId"] as? JsonPrimitive)?.contentOrNull?.trim()
        val toWalletId = (obj["toWalletId"] as? JsonPrimitive)?.contentOrNull?.trim()
        val note = (obj["note"] as? JsonPrimitive)?.contentOrNull
        val createdAt = (obj["createdAt"] as? JsonPrimitive)?.contentOrNull?.toLongOrNull() ?: 0L
        val updatedAt = (obj["updatedAt"] as? JsonPrimitive)?.contentOrNull?.toLongOrNull() ?: 0L
        val deleted = (obj["deleted"] as? JsonPrimitive)?.contentOrNull?.toBooleanStrictOrNull() ?: false
        val dummy = (obj["dummy"] as? JsonPrimitive)?.contentOrNull?.toBooleanStrictOrNull() ?: false

        return TransactionDto(
            id = id,
            type = type,
            amount = amount,
            date = date,
            time = time,
            category = category,
            subcategory = subcategory,
            walletId = walletId,
            toWalletId = toWalletId,
            note = note,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deleted = deleted,
            dummy = dummy
        )
    }

    private fun parseCategoryItemElement(elem: JsonElement, defaultOrder: Int): CategoryItem? {
        return when (elem) {
            is JsonPrimitive -> {
                val name = elem.content.trim()
                if (name.isNotEmpty()) CategoryItem(name = name, order = defaultOrder) else null
            }
            is JsonObject -> {
                val name = (elem["name"] as? JsonPrimitive)?.contentOrNull?.trim()
                if (!name.isNullOrEmpty()) {
                    val order = (elem["order"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull()
                        ?: (elem["urutan"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull()
                        ?: defaultOrder
                    val icon = (elem["icon"] as? JsonPrimitive)?.contentOrNull?.trim()?.takeIf { it.isNotEmpty() }
                    CategoryItem(name = name, order = order, icon = icon)
                } else null
            }
            else -> null
        }
    }

    private fun parseCategoryList(arr: JsonArray?): List<CategoryItem> {
        if (arr == null) return emptyList()
        val list = ArrayList<CategoryItem>()
        arr.forEachIndexed { idx, elem ->
            val item = parseCategoryItemElement(elem, idx)
            if (item != null) list.add(item)
        }
        return list
    }

    private fun parseCategoriesSafely(obj: JsonObject): CategoriesData {
        val income = parseCategoryList(obj["income"] as? JsonArray)
        val expense = parseCategoryList(obj["expense"] as? JsonArray)

        val subsObj = obj["subcategories"] as? JsonObject
        val subIncomeMap = HashMap<String, List<CategoryItem>>()
        val subExpenseMap = HashMap<String, List<CategoryItem>>()

        (subsObj?.get("income") as? JsonObject)?.forEach { (catName, elem) ->
            if (elem is JsonArray) {
                subIncomeMap[catName] = parseCategoryList(elem)
            }
        }
        (subsObj?.get("expense") as? JsonObject)?.forEach { (catName, elem) ->
            if (elem is JsonArray) {
                subExpenseMap[catName] = parseCategoryList(elem)
            }
        }

        val updatedAt = (obj["updatedAt"] as? JsonPrimitive)?.contentOrNull?.toLongOrNull() ?: 0L
        return CategoriesData(
            income = income,
            expense = expense,
            subcategories = SubcategoriesGroup(income = subIncomeMap, expense = subExpenseMap),
            updatedAt = updatedAt
        )
    }

    private fun parseBudgetsSafely(obj: JsonObject): BudgetDto {
        val itemsMap = HashMap<String, Long>()
        val itemsObj = obj["items"] as? JsonObject
        itemsObj?.forEach { (k, elem) ->
            if (elem is JsonPrimitive) {
                try {
                    itemsMap[k] = Uang.dariJsonPrimitive(elem)
                } catch (_: Exception) {
                    elem.content.toDoubleOrNull()?.let {
                        val cents = BigDecimal.valueOf(it).multiply(BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).longValueExact()
                        itemsMap[k] = cents
                    }
                }
            }
        }

        val subsMap = HashMap<String, Map<String, Long>>()
        val subsObj = obj["subcategories"] as? JsonObject
        subsObj?.forEach { (cat, subGroup) ->
            if (subGroup is JsonObject) {
                val subItems = HashMap<String, Long>()
                subGroup.forEach { (subKey, elem) ->
                    if (elem is JsonPrimitive) {
                        try {
                            subItems[subKey] = Uang.dariJsonPrimitive(elem)
                        } catch (_: Exception) {
                            elem.content.toDoubleOrNull()?.let {
                                val cents = BigDecimal.valueOf(it).multiply(BigDecimal("100")).setScale(0, RoundingMode.HALF_UP).longValueExact()
                                subItems[subKey] = cents
                            }
                        }
                    }
                }
                if (subItems.isNotEmpty()) {
                    subsMap[cat] = subItems
                }
            }
        }

        val updatedAt = (obj["updatedAt"] as? JsonPrimitive)?.contentOrNull?.toLongOrNull() ?: 0L
        return BudgetDto(items = itemsMap, subcategories = subsMap, updatedAt = updatedAt)
    }

    private fun parseWalletsSafely(arr: JsonArray): List<WalletDto> {
        val list = ArrayList<WalletDto>()
        arr.forEachIndexed { idx, elem ->
            if (elem is JsonObject) {
                val dto = try {
                    JsonBackupManager.json.decodeFromJsonElement<WalletDto>(elem)
                } catch (_: Exception) {
                    val id = (elem["id"] as? JsonPrimitive)?.contentOrNull?.trim()
                    val name = (elem["name"] as? JsonPrimitive)?.contentOrNull?.trim()
                    if (!id.isNullOrEmpty() && !name.isNullOrEmpty()) {
                        val curr = (elem["currency"] as? JsonPrimitive)?.contentOrNull?.trim() ?: "IDR"
                        val order = (elem["order"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull()
                            ?: (elem["urutan"] as? JsonPrimitive)?.contentOrNull?.toIntOrNull()
                            ?: idx
                        val createdAt = (elem["createdAt"] as? JsonPrimitive)?.contentOrNull?.toLongOrNull() ?: 0L
                        val updatedAt = (elem["updatedAt"] as? JsonPrimitive)?.contentOrNull?.toLongOrNull() ?: 0L
                        val deleted = (elem["deleted"] as? JsonPrimitive)?.contentOrNull?.toBooleanStrictOrNull() ?: false
                        WalletDto(
                            id = id,
                            name = name,
                            currency = if (curr == "JPY" || curr == "USD") curr else "IDR",
                            order = order,
                            createdAt = createdAt,
                            updatedAt = updatedAt,
                            deleted = deleted
                        )
                    } else null
                }
                if (dto != null) list.add(dto)
            }
        }
        return list
    }
}

/**
 * Ekstensi helper untuk mengonversi Task Google Play Services menjadi suspend function.
 */
suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { continuation ->
    addOnSuccessListener { result ->
        if (continuation.isActive) {
            continuation.resume(result)
        }
    }
    addOnFailureListener { exception ->
        if (continuation.isActive) {
            continuation.resumeWithException(exception)
        }
    }
    addOnCanceledListener {
        continuation.cancel()
    }
}
