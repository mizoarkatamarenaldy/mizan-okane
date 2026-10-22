package com.mizoar.mizanokane.data.sync

import android.accounts.Account
import android.app.Activity
import android.app.PendingIntent
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
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
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import com.mizoar.mizanokane.data.repository.MizanRepository
import java.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
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
            try {
                JsonBackupManager.json.decodeFromString<BackupRootDto>(jsonText)
            } catch (e: Exception) {
                throw IllegalStateException("Format file sinkron di Drive tidak dikenali.", e)
            }
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
            finalWallets = JsonBackupManager.mergeWalletsList(localWallets, remoteWallets)

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
