package com.mizoar.mizanokane.data.repository

import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.SampleData
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Implementasi in-memory dari [MizanRepository].
 * Digunakan untuk unit testing JVM mandiri dan preview antarmuka.
 */
class InMemoryMizanRepository : MizanRepository {

    private val transactionsMap = LinkedHashMap<String, TransactionEntity>()
    private val transactionsFlow = MutableStateFlow<List<TransactionEntity>>(emptyList())

    private val walletsMap = LinkedHashMap<String, WalletEntity>()
    private val walletsFlow = MutableStateFlow<List<WalletEntity>>(emptyList())

    private var categoriesData = DefaultData.DEFAULT_CATEGORIES
    private val categoriesFlow = MutableStateFlow(categoriesData)

    private var budgetsData = BudgetsData(updatedAt = 0L)
    private val budgetsFlow = MutableStateFlow(budgetsData)

    private var settingsData = DefaultData.DEFAULT_SETTINGS
    private val settingsFlow = MutableStateFlow(settingsData)

    private val metaMap = HashMap<String, String>()

    // Mode Data Contoh (Demo)
    private var demoMode = false
    private val demoModeFlow = MutableStateFlow(false)

    private val demoTransactionsMap = LinkedHashMap<String, TransactionEntity>()
    private val demoWalletsMap = LinkedHashMap<String, WalletEntity>()
    private var demoCategoriesData = SampleData.SAMPLE_CATEGORIES
    private var demoBudgetsData = SampleData.SAMPLE_BUDGETS

    init {
        DefaultData.DEFAULT_WALLETS.forEach { walletsMap[it.id] = it }
        walletsFlow.value = walletsMap.values.toList()
    }

    // --- Transaksi ---

    override fun getAllTransactionsFlow(): Flow<List<TransactionEntity>> = transactionsFlow.asStateFlow()

    override suspend fun getAllTransactions(): List<TransactionEntity> {
        return if (demoMode) demoTransactionsMap.values.toList() else transactionsMap.values.toList()
    }

    override suspend fun getTransactionById(id: String): TransactionEntity? {
        return if (demoMode) demoTransactionsMap[id] else transactionsMap[id]
    }

    override suspend fun saveTransaction(tx: TransactionEntity) {
        if (demoMode) {
            demoTransactionsMap[tx.id] = tx
            transactionsFlow.value = demoTransactionsMap.values.toList()
        } else {
            transactionsMap[tx.id] = tx
            transactionsFlow.value = transactionsMap.values.toList()
        }
    }

    override suspend fun markTransactionDeleted(id: String, updatedAt: Long) {
        if (demoMode) {
            val existing = demoTransactionsMap[id]
            if (existing != null) {
                demoTransactionsMap[id] = existing.copy(deleted = true, updatedAt = updatedAt)
                transactionsFlow.value = demoTransactionsMap.values.toList()
            }
        } else {
            val existing = transactionsMap[id]
            if (existing != null) {
                val updated = existing.copy(deleted = true, updatedAt = updatedAt)
                transactionsMap[id] = updated
                transactionsFlow.value = transactionsMap.values.toList()
            }
        }
    }

    override suspend fun replaceAllTransactions(transactions: List<TransactionEntity>) {
        if (demoMode) {
            demoTransactionsMap.clear()
            transactions.forEach { demoTransactionsMap[it.id] = it }
            transactionsFlow.value = demoTransactionsMap.values.toList()
        } else {
            transactionsMap.clear()
            transactions.forEach { transactionsMap[it.id] = it }
            transactionsFlow.value = transactionsMap.values.toList()
        }
    }

    // --- Dompet ---

    override fun getAllWalletsFlow(): Flow<List<WalletEntity>> = walletsFlow.asStateFlow()

    override suspend fun getAllWallets(): List<WalletEntity> {
        return if (demoMode) demoWalletsMap.values.sortedBy { it.order } else walletsMap.values.sortedBy { it.order }
    }

    override suspend fun getWalletById(id: String): WalletEntity? {
        return if (demoMode) demoWalletsMap[id] else walletsMap[id]
    }

    override suspend fun saveWallet(wallet: WalletEntity) {
        if (demoMode) {
            demoWalletsMap[wallet.id] = wallet
            walletsFlow.value = demoWalletsMap.values.sortedBy { it.order }
        } else {
            walletsMap[wallet.id] = wallet
            walletsFlow.value = walletsMap.values.sortedBy { it.order }
        }
    }

    override suspend fun saveWallets(wallets: List<WalletEntity>) {
        if (demoMode) {
            wallets.forEach { demoWalletsMap[it.id] = it }
            walletsFlow.value = demoWalletsMap.values.sortedBy { it.order }
        } else {
            wallets.forEach { walletsMap[it.id] = it }
            walletsFlow.value = walletsMap.values.sortedBy { it.order }
        }
    }

    override suspend fun markWalletDeleted(id: String, updatedAt: Long) {
        if (demoMode) {
            val existing = demoWalletsMap[id]
            if (existing != null) {
                demoWalletsMap[id] = existing.copy(deleted = true, updatedAt = updatedAt)
                walletsFlow.value = demoWalletsMap.values.sortedBy { it.order }
            }
        } else {
            val existing = walletsMap[id]
            if (existing != null) {
                val updated = existing.copy(deleted = true, updatedAt = updatedAt)
                walletsMap[id] = updated
                walletsFlow.value = walletsMap.values.sortedBy { it.order }
            }
        }
    }

    override suspend fun replaceAllWallets(wallets: List<WalletEntity>) {
        if (demoMode) {
            demoWalletsMap.clear()
            wallets.forEach { demoWalletsMap[it.id] = it }
            walletsFlow.value = demoWalletsMap.values.sortedBy { it.order }
        } else {
            walletsMap.clear()
            wallets.forEach { walletsMap[it.id] = it }
            walletsFlow.value = walletsMap.values.sortedBy { it.order }
        }
    }

    // --- Kategori & Sub Kategori ---

    override fun getCategoriesFlow(): Flow<CategoriesData> = categoriesFlow.asStateFlow()

    override suspend fun getCategories(): CategoriesData = if (demoMode) demoCategoriesData else categoriesData

    override suspend fun saveCategories(categories: CategoriesData) {
        if (demoMode) {
            demoCategoriesData = categories
            categoriesFlow.value = categories
        } else {
            categoriesData = categories
            categoriesFlow.value = categories
            metaMap["categories_updated_at"] = categories.updatedAt.toString()
        }
    }

    // --- Anggaran (Budget) ---

    override fun getBudgetsFlow(): Flow<BudgetsData> = budgetsFlow.asStateFlow()

    override suspend fun getBudgets(): BudgetsData = if (demoMode) demoBudgetsData else budgetsData

    override suspend fun saveBudgets(budgets: BudgetsData) {
        if (demoMode) {
            demoBudgetsData = budgets
            budgetsFlow.value = budgets
        } else {
            budgetsData = budgets
            budgetsFlow.value = budgets
            metaMap["budgets_updated_at"] = budgets.updatedAt.toString()
        }
    }

    // --- Pengaturan (Settings) ---

    override fun getSettingsFlow(): Flow<SettingsEntity> = settingsFlow.asStateFlow()

    override suspend fun getSettings(): SettingsEntity = settingsData

    override suspend fun saveSettings(settings: SettingsEntity) {
        settingsData = settings
        settingsFlow.value = settings
    }

    // --- Metadata ---

    override suspend fun getMeta(key: String): String? = metaMap[key]

    override suspend fun setMeta(key: String, value: String) {
        metaMap[key] = value
    }

    override suspend fun deleteMeta(key: String) {
        metaMap.remove(key)
    }

    // --- Mode Data Contoh (Demo) & Data Asli ---

    override fun isDemoModeFlow(): Flow<Boolean> = demoModeFlow.asStateFlow()

    override suspend fun isDemoMode(): Boolean = demoMode

    override suspend fun activateSampleData(referenceDate: LocalDate) {
        demoMode = true
        demoTransactionsMap.clear()
        SampleData.getSampleTransactions(referenceDate).forEach { demoTransactionsMap[it.id] = it }

        demoWalletsMap.clear()
        SampleData.SAMPLE_WALLETS.forEach { demoWalletsMap[it.id] = it }

        demoCategoriesData = SampleData.SAMPLE_CATEGORIES
        demoBudgetsData = SampleData.SAMPLE_BUDGETS

        demoModeFlow.value = true
        transactionsFlow.value = demoTransactionsMap.values.toList()
        walletsFlow.value = demoWalletsMap.values.sortedBy { it.order }
        categoriesFlow.value = demoCategoriesData
        budgetsFlow.value = demoBudgetsData
    }

    override suspend fun clearSampleData() {
        demoMode = false
        demoTransactionsMap.clear()
        demoWalletsMap.clear()
        demoModeFlow.value = false

        transactionsFlow.value = transactionsMap.values.toList()
        walletsFlow.value = walletsMap.values.sortedBy { it.order }
        categoriesFlow.value = categoriesData
        budgetsFlow.value = budgetsData
    }

    override suspend fun getRealTransactions(): List<TransactionEntity> = transactionsMap.values.toList()

    override suspend fun getRealWallets(): List<WalletEntity> = walletsMap.values.sortedBy { it.order }

    override suspend fun getRealCategories(): CategoriesData = categoriesData

    override suspend fun getRealBudgets(): BudgetsData = budgetsData

    // --- Kembalikan ke bawaan (Factory Reset level data) ---

    override suspend fun resetToDefaults() {
        demoMode = false
        demoTransactionsMap.clear()
        demoWalletsMap.clear()
        demoModeFlow.value = false

        transactionsMap.clear()
        transactionsFlow.value = emptyList()

        categoriesData = DefaultData.DEFAULT_CATEGORIES
        categoriesFlow.value = categoriesData
        metaMap["categories_updated_at"] = "0"

        budgetsData = BudgetsData(updatedAt = 0L)
        budgetsFlow.value = budgetsData
        metaMap["budgets_updated_at"] = "0"

        walletsMap.clear()
        DefaultData.DEFAULT_WALLETS.forEach { walletsMap[it.id] = it.copy(createdAt = System.currentTimeMillis()) }
        walletsFlow.value = walletsMap.values.toList()

        settingsData = DefaultData.DEFAULT_SETTINGS
        settingsFlow.value = settingsData

        metaMap["localRev"] = "0"
    }
}
