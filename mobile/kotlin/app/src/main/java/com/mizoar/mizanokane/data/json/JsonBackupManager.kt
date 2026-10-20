package com.mizoar.mizanokane.data.json

import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.CategoryItem
import com.mizoar.mizanokane.data.model.SubcategoriesGroup
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import com.mizoar.mizanokane.data.repository.MizanRepository
import java.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

/**
 * Hasil dari eksekusi impor data cadangan JSON.
 */
data class ImportResult(
    val success: Boolean,
    val transactionsAdded: Int = 0,
    val transactionsUpdated: Int = 0,
    val walletsChanged: Boolean = false,
    val categoriesChanged: Boolean = false,
    val budgetsChanged: Boolean = false,
    val settingsChanged: Boolean = false,
    val message: String = ""
)

/**
 * Pengelola ekspor dan impor file cadangan JSON aplikasi Mizan Okane.
 * Format berkas dan logika penggabungan konflik (Last-Write-Wins) sama persis dengan versi web.
 */
object JsonBackupManager {

    @OptIn(ExperimentalSerializationApi::class)
    val json = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Mengekspor seluruh data aktif lokal ke teks JSON berformat cadangan resmi versi 2.
     * Transaksi dummy tidak diikutsertakan.
     */
    suspend fun exportToJson(repository: MizanRepository, exportedAt: String? = null): String {
        val realTransactions = repository.getRealTransactions()
            .filter { !it.dummy }
            .map { TransactionDto.fromEntity(it) }

        val realWallets = repository.getRealWallets()
            .map { WalletDto.fromEntity(it) }

        val realCategories = repository.getRealCategories()
        val realBudgets = BudgetDto.fromBudgetsData(repository.getRealBudgets())
        val realSettings = SettingsDto.fromEntity(repository.getSettings())

        val rootDto = BackupRootDto(
            app = "mizan-okane",
            version = 2,
            schemaVersion = 2,
            exportedAt = exportedAt ?: Instant.now().toString(),
            transactions = realTransactions,
            wallets = realWallets,
            categories = realCategories,
            budgets = realBudgets,
            settings = realSettings
        )

        return json.encodeToString(BackupRootDto.serializer(), rootDto)
    }

    /**
     * Mengimpor teks JSON cadangan ke dalam basis data lokal.
     * Mendukung format array versi 1 dan objek lengkap versi 2.
     * Mengikuti aturan penggabungan Last-Write-Wins (updatedAt) sesuai web.
     */
    suspend fun importFromJson(jsonContent: String, repository: MizanRepository): ImportResult {
        if (repository.isDemoMode()) {
            return ImportResult(
                success = false,
                message = "Tidak dapat mengimpor saat dalam mode data contoh. Silakan keluar dari mode data contoh terlebih dahulu."
            )
        }

        val trimmed = jsonContent.trim()
        if (trimmed.isEmpty()) {
            return ImportResult(success = false, message = "Konten JSON kosong")
        }

        val rootElement: JsonElement = try {
            json.parseToJsonElement(trimmed)
        } catch (e: Exception) {
            return ImportResult(success = false, message = "Format JSON tidak valid: ${e.message}")
        }

        var rawTransactions: JsonArray? = null
        var rawCategories: JsonObject? = null
        var rawBudgets: JsonObject? = null
        var rawWallets: JsonArray? = null
        var rawSettings: JsonObject? = null

        if (rootElement is JsonArray) {
            rawTransactions = rootElement
        } else if (rootElement is JsonObject) {
            rawTransactions = rootElement["transactions"] as? JsonArray
            rawCategories = rootElement["categories"] as? JsonObject
            rawBudgets = rootElement["budgets"] as? JsonObject
            rawWallets = rootElement["wallets"] as? JsonArray
            rawSettings = rootElement["settings"] as? JsonObject
        }

        if (rawTransactions == null && rawCategories == null && rawBudgets == null && rawWallets == null && rawSettings == null) {
            return ImportResult(
                success = false,
                message = "Format file cadangan tidak dikenali. Pastikan file berisi data cadangan mizan-okane."
            )
        }

        // 1. Validasi & Penggabungan Transaksi
        var txAdded = 0
        var txUpdated = 0

        if (rawTransactions != null) {
            val validImportedTx = ArrayList<TransactionEntity>()
            for (elem in rawTransactions) {
                if (elem is JsonObject) {
                    try {
                        val dto = json.decodeFromJsonElement<TransactionDto>(elem)
                        if (isValidTransaction(dto) && !dto.dummy) {
                            validImportedTx.add(dto.toEntity())
                        }
                    } catch (e: Exception) {
                        val isDecimalError = (e is IllegalArgumentException && e.message?.contains("desimal") == true) ||
                            (e.cause is IllegalArgumentException && e.cause?.message?.contains("desimal") == true)
                        if (isDecimalError) {
                            throw (e as? IllegalArgumentException ?: (e.cause as? IllegalArgumentException) ?: e)
                        }
                    }
                }
            }

            if (rawTransactions.isNotEmpty() && validImportedTx.isEmpty()) {
                return ImportResult(
                    success = false,
                    message = "Tidak ada transaksi valid yang ditemukan di dalam file cadangan."
                )
            }

            val localTxList = repository.getAllTransactions()
            val txMap = LinkedHashMap<String, TransactionEntity>()
            for (tx in localTxList) {
                txMap[tx.id] = tx
            }

            for (impTx in validImportedTx) {
                val existing = txMap[impTx.id]
                if (existing == null) {
                    txMap[impTx.id] = impTx
                    txAdded++
                } else {
                    val impTime = if (impTx.updatedAt > 0L) impTx.updatedAt else impTx.createdAt
                    val existTime = if (existing.updatedAt > 0L) existing.updatedAt else existing.createdAt
                    if (impTime > existTime) {
                        txMap[impTx.id] = impTx
                        txUpdated++
                    }
                }
            }

            if (txAdded > 0 || txUpdated > 0) {
                repository.replaceAllTransactions(txMap.values.toList())
            }
        }

        // 2. Penggabungan Kategori
        var categoriesChanged = false
        if (rawCategories != null) {
            try {
                val importedCats = json.decodeFromJsonElement<CategoriesData>(rawCategories)
                val localCats = repository.getCategories()
                val remoteCatTime = importedCats.updatedAt
                val localCatTime = localCats.updatedAt

                if (remoteCatTime > localCatTime || (localCatTime == 0L && (importedCats.income.isNotEmpty() || importedCats.expense.isNotEmpty()))) {
                    val cleanCats = sanitizeCategories(importedCats)
                    repository.saveCategories(cleanCats)
                    categoriesChanged = true
                }
            } catch (e: Exception) {
                // Abaikan kesalahan parsial kategori jika rusak
            }
        }

        // 3. Penggabungan Budget
        var budgetsChanged = false
        if (rawBudgets != null) {
            try {
                val importedBudgetsDto = json.decodeFromJsonElement<BudgetDto>(rawBudgets)
                val importedBudgets = importedBudgetsDto.toBudgetsData()
                val localBudgets = repository.getBudgets()
                val remoteBudgetTime = importedBudgets.updatedAt
                val localBudgetTime = localBudgets.updatedAt

                val hasRawItems = importedBudgets.items.isNotEmpty()
                val hasRawSubs = importedBudgets.subcategories.isNotEmpty()

                if (remoteBudgetTime > localBudgetTime || (localBudgetTime == 0L && (hasRawItems || hasRawSubs))) {
                    repository.saveBudgets(importedBudgets)
                    budgetsChanged = true
                }
            } catch (e: Exception) {
                val isDecimalError = (e is IllegalArgumentException && e.message?.contains("desimal") == true) ||
                    (e.cause is IllegalArgumentException && e.cause?.message?.contains("desimal") == true)
                if (isDecimalError) {
                    throw (e as? IllegalArgumentException ?: (e.cause as? IllegalArgumentException) ?: e)
                }
            }
        }

        // 4. Penggabungan Dompet
        var walletsChanged = false
        if (rawWallets != null && rawWallets.isNotEmpty()) {
            try {
                val importedWallets = rawWallets.mapNotNull {
                    if (it is JsonObject) {
                        json.decodeFromJsonElement<WalletDto>(it).toEntity()
                    } else null
                }
                val localWallets = repository.getAllWallets()
                val finalWallets = mergeWalletsList(localWallets, importedWallets)
                if (walletsDiffer(localWallets, finalWallets)) {
                    repository.replaceAllWallets(finalWallets)
                    walletsChanged = true
                }
            } catch (e: Exception) {
                // Abaikan jika struktur dompet rusak
            }
        }

        // 5. Penggabungan Pengaturan (Settings)
        var settingsChanged = false
        if (rawSettings != null) {
            try {
                val importedSettings = json.decodeFromJsonElement<SettingsDto>(rawSettings).toEntity()
                val localSettings = repository.getSettings()
                val remoteSettingsTime = importedSettings.updatedAt
                val localSettingsTime = localSettings.updatedAt

                if (remoteSettingsTime > localSettingsTime || (localSettingsTime == 0L && (importedSettings.theme.isNotBlank() || importedSettings.timeFormat.isNotBlank()))) {
                    val targetTime = if (remoteSettingsTime > 0L) remoteSettingsTime else System.currentTimeMillis()
                    repository.saveSettings(importedSettings.copy(updatedAt = targetTime))
                    settingsChanged = true
                }
            } catch (e: Exception) {
                // Abaikan kesalahan parsial pengaturan
            }
        }

        return ImportResult(
            success = true,
            transactionsAdded = txAdded,
            transactionsUpdated = txUpdated,
            walletsChanged = walletsChanged,
            categoriesChanged = categoriesChanged,
            budgetsChanged = budgetsChanged,
            settingsChanged = settingsChanged,
            message = "Impor data berhasil diproses."
        )
    }

    private fun isValidTransaction(dto: TransactionDto): Boolean {
        if (dto.id.trim().isEmpty()) return false
        if (dto.type != "income" && dto.type != "expense" && dto.type != "transfer" && dto.type != "penyesuaian") {
            return false
        }
        if (dto.type == "penyesuaian") {
            if (dto.amount == 0L) return false
        } else {
            if (dto.amount <= 0L) return false
        }
        if (dto.date.trim().isEmpty()) return false
        return true
    }

    private fun sanitizeCategoryList(list: List<CategoryItem>, defaultList: List<CategoryItem>): List<CategoryItem> {
        val result = ArrayList<CategoryItem>()
        val seen = HashSet<String>()
        val source = if (list.isEmpty()) defaultList else list
        source.forEachIndexed { idx, item ->
            val trimmedName = item.name.trim()
            if (trimmedName.isNotEmpty() && !seen.contains(trimmedName.lowercase())) {
                seen.add(trimmedName.lowercase())
                result.add(CategoryItem(name = trimmedName, order = if (item.order >= 0) item.order else idx, icon = item.icon?.trim()))
            }
        }
        return result.sortedBy { it.order }
    }

    private fun sanitizeCategories(data: CategoriesData): CategoriesData {
        val cleanIncome = sanitizeCategoryList(data.income, DefaultData.DEFAULT_CATEGORIES.income)
        val cleanExpense = sanitizeCategoryList(data.expense, DefaultData.DEFAULT_CATEGORIES.expense)

        val cleanSubIncome = data.subcategories.income.mapValues { (_, subs) ->
            subs.filter { it.name.trim().isNotEmpty() }.distinctBy { it.name.lowercase() }.sortedBy { it.order }
        }
        val cleanSubExpense = data.subcategories.expense.mapValues { (_, subs) ->
            subs.filter { it.name.trim().isNotEmpty() }.distinctBy { it.name.lowercase() }.sortedBy { it.order }
        }

        return CategoriesData(
            income = cleanIncome,
            expense = cleanExpense,
            subcategories = SubcategoriesGroup(
                income = cleanSubIncome,
                expense = cleanSubExpense
            ),
            updatedAt = if (data.updatedAt > 0L) data.updatedAt else System.currentTimeMillis()
        )
    }

    fun mergeWalletsList(localWallets: List<WalletEntity>, importedWallets: List<WalletEntity>): List<WalletEntity> {
        val map = LinkedHashMap<String, WalletEntity>()
        importedWallets.forEachIndexed { i, w ->
            if (w.id.isNotBlank()) {
                map[w.id] = w.copy(order = if (w.order >= 0) w.order else i)
            }
        }
        localWallets.forEachIndexed { i, w ->
            if (w.id.isNotBlank()) {
                val existing = map[w.id]
                if (existing == null || w.updatedAt >= existing.updatedAt) {
                    map[w.id] = w.copy(order = if (w.order >= 0) w.order else (existing?.order ?: i))
                }
            }
        }
        if (!map.containsKey(DefaultData.DEFAULT_WALLET_ID)) {
            map[DefaultData.DEFAULT_WALLET_ID] = DefaultData.DEFAULT_WALLETS.first()
        }
        return map.values.sortedBy { it.order }
    }

    fun walletsDiffer(w1: List<WalletEntity>, w2: List<WalletEntity>): Boolean {
        if (w1.size != w2.size) return true
        val m1 = w1.associateBy { it.id }
        for (item in w2) {
            val exist = m1[item.id] ?: return true
            if (exist.name != item.name || exist.currency != item.currency || exist.deleted != item.deleted ||
                exist.updatedAt != item.updatedAt || exist.order != item.order
            ) {
                return true
            }
        }
        return false
    }
}
