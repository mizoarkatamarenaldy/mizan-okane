package com.mizoar.mizanokane.data.repository

import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

/**
 * Repository tunggal untuk operasi dasar lapisan data aplikasi Mizan Okane.
 * Menyediakan akses terpadu ke transaksi, dompet, kategori, budget, pengaturan, dan metadata.
 */
interface MizanRepository {

    // --- Transaksi ---
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>
    suspend fun getAllTransactions(): List<TransactionEntity>
    suspend fun getTransactionById(id: String): TransactionEntity?
    suspend fun saveTransaction(tx: TransactionEntity)
    suspend fun markTransactionDeleted(id: String, updatedAt: Long = System.currentTimeMillis())
    suspend fun replaceAllTransactions(transactions: List<TransactionEntity>)

    // --- Dompet ---
    fun getAllWalletsFlow(): Flow<List<WalletEntity>>
    suspend fun getAllWallets(): List<WalletEntity>
    suspend fun getWalletById(id: String): WalletEntity?
    suspend fun saveWallet(wallet: WalletEntity)
    suspend fun saveWallets(wallets: List<WalletEntity>)
    suspend fun markWalletDeleted(id: String, updatedAt: Long = System.currentTimeMillis())
    suspend fun replaceAllWallets(wallets: List<WalletEntity>)

    // --- Kategori & Sub Kategori ---
    fun getCategoriesFlow(): Flow<CategoriesData>
    suspend fun getCategories(): CategoriesData
    suspend fun saveCategories(categories: CategoriesData)

    // --- Anggaran (Budget) ---
    fun getBudgetsFlow(): Flow<BudgetsData>
    suspend fun getBudgets(): BudgetsData
    suspend fun saveBudgets(budgets: BudgetsData)

    // --- Pengaturan (Settings) ---
    fun getSettingsFlow(): Flow<SettingsEntity>
    suspend fun getSettings(): SettingsEntity
    suspend fun saveSettings(settings: SettingsEntity)

    // --- Metadata ---
    suspend fun getMeta(key: String): String?
    suspend fun setMeta(key: String, value: String)
    suspend fun deleteMeta(key: String)

    // --- Mode Data Contoh (Demo) & Data Asli ---
    fun isDemoModeFlow(): Flow<Boolean>
    suspend fun isDemoMode(): Boolean
    suspend fun activateSampleData(referenceDate: LocalDate = LocalDate.now())
    suspend fun clearSampleData()

    suspend fun getRealTransactions(): List<TransactionEntity>
    suspend fun getRealWallets(): List<WalletEntity>
    suspend fun getRealCategories(): CategoriesData
    suspend fun getRealBudgets(): BudgetsData

    // --- Kembalikan ke bawaan (Factory Reset level data) ---
    suspend fun resetToDefaults()
}
