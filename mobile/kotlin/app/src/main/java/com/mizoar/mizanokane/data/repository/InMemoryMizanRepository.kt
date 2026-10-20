package com.mizoar.mizanokane.data.repository

import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
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

    init {
        DefaultData.DEFAULT_WALLETS.forEach { walletsMap[it.id] = it }
        walletsFlow.value = walletsMap.values.toList()
    }

    // --- Transaksi ---

    override fun getAllTransactionsFlow(): Flow<List<TransactionEntity>> = transactionsFlow.asStateFlow()

    override suspend fun getAllTransactions(): List<TransactionEntity> {
        return transactionsMap.values.toList()
    }

    override suspend fun getTransactionById(id: String): TransactionEntity? {
        return transactionsMap[id]
    }

    override suspend fun saveTransaction(tx: TransactionEntity) {
        transactionsMap[tx.id] = tx
        transactionsFlow.value = transactionsMap.values.toList()
    }

    override suspend fun markTransactionDeleted(id: String, updatedAt: Long) {
        val existing = transactionsMap[id]
        if (existing != null) {
            val updated = existing.copy(deleted = true, updatedAt = updatedAt)
            transactionsMap[id] = updated
            transactionsFlow.value = transactionsMap.values.toList()
        }
    }

    override suspend fun replaceAllTransactions(transactions: List<TransactionEntity>) {
        transactionsMap.clear()
        transactions.forEach { transactionsMap[it.id] = it }
        transactionsFlow.value = transactionsMap.values.toList()
    }

    // --- Dompet ---

    override fun getAllWalletsFlow(): Flow<List<WalletEntity>> = walletsFlow.asStateFlow()

    override suspend fun getAllWallets(): List<WalletEntity> {
        return walletsMap.values.sortedBy { it.order }
    }

    override suspend fun getWalletById(id: String): WalletEntity? {
        return walletsMap[id]
    }

    override suspend fun saveWallet(wallet: WalletEntity) {
        walletsMap[wallet.id] = wallet
        walletsFlow.value = walletsMap.values.sortedBy { it.order }
    }

    override suspend fun saveWallets(wallets: List<WalletEntity>) {
        wallets.forEach { walletsMap[it.id] = it }
        walletsFlow.value = walletsMap.values.sortedBy { it.order }
    }

    override suspend fun markWalletDeleted(id: String, updatedAt: Long) {
        val existing = walletsMap[id]
        if (existing != null) {
            val updated = existing.copy(deleted = true, updatedAt = updatedAt)
            walletsMap[id] = updated
            walletsFlow.value = walletsMap.values.sortedBy { it.order }
        }
    }

    override suspend fun replaceAllWallets(wallets: List<WalletEntity>) {
        walletsMap.clear()
        wallets.forEach { walletsMap[it.id] = it }
        walletsFlow.value = walletsMap.values.sortedBy { it.order }
    }

    // --- Kategori & Sub Kategori ---

    override fun getCategoriesFlow(): Flow<CategoriesData> = categoriesFlow.asStateFlow()

    override suspend fun getCategories(): CategoriesData = categoriesData

    override suspend fun saveCategories(categories: CategoriesData) {
        categoriesData = categories
        categoriesFlow.value = categories
        metaMap["categories_updated_at"] = categories.updatedAt.toString()
    }

    // --- Anggaran (Budget) ---

    override fun getBudgetsFlow(): Flow<BudgetsData> = budgetsFlow.asStateFlow()

    override suspend fun getBudgets(): BudgetsData = budgetsData

    override suspend fun saveBudgets(budgets: BudgetsData) {
        budgetsData = budgets
        budgetsFlow.value = budgets
        metaMap["budgets_updated_at"] = budgets.updatedAt.toString()
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

    // --- Kembalikan ke bawaan (Factory Reset level data) ---

    override suspend fun resetToDefaults() {
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
