package com.mizoar.mizanokane.data.repository

import androidx.room.withTransaction
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.local.AppDatabase
import com.mizoar.mizanokane.data.model.BudgetEntity
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.CategoryEntity
import com.mizoar.mizanokane.data.model.CategoryItem
import com.mizoar.mizanokane.data.model.MetaEntity
import com.mizoar.mizanokane.data.model.SettingsEntity
import com.mizoar.mizanokane.data.model.SubcategoriesGroup
import com.mizoar.mizanokane.data.model.SubcategoryBudgetEntity
import com.mizoar.mizanokane.data.model.SubcategoryEntity
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * Implementasi [MizanRepository] berbasis Room Database.
 */
class RoomMizanRepository(private val db: AppDatabase) : MizanRepository {

    // --- Transaksi ---

    override fun getAllTransactionsFlow(): Flow<List<TransactionEntity>> {
        return db.transactionDao().getAllFlow()
    }

    override suspend fun getAllTransactions(): List<TransactionEntity> {
        return db.transactionDao().getAll()
    }

    override suspend fun getTransactionById(id: String): TransactionEntity? {
        return db.transactionDao().getById(id)
    }

    override suspend fun saveTransaction(tx: TransactionEntity) {
        db.transactionDao().insert(tx)
    }

    override suspend fun markTransactionDeleted(id: String, updatedAt: Long) {
        db.transactionDao().markDeleted(id, updatedAt)
    }

    override suspend fun replaceAllTransactions(transactions: List<TransactionEntity>) {
        db.withTransaction {
            db.transactionDao().deleteAll()
            db.transactionDao().insertAll(transactions)
        }
    }

    // --- Dompet ---

    override fun getAllWalletsFlow(): Flow<List<WalletEntity>> {
        return db.walletDao().getAllFlow().map { list ->
            if (list.isEmpty()) {
                val initial = DefaultData.DEFAULT_WALLETS.map { it.copy(createdAt = System.currentTimeMillis()) }
                db.walletDao().insertAll(initial)
                initial
            } else {
                list
            }
        }
    }

    override suspend fun getAllWallets(): List<WalletEntity> {
        val list = db.walletDao().getAll()
        return if (list.isEmpty()) {
            val initial = DefaultData.DEFAULT_WALLETS.map { it.copy(createdAt = System.currentTimeMillis()) }
            db.walletDao().insertAll(initial)
            initial
        } else {
            list
        }
    }

    override suspend fun getWalletById(id: String): WalletEntity? {
        return db.walletDao().getById(id)
    }

    override suspend fun saveWallet(wallet: WalletEntity) {
        db.walletDao().insert(wallet)
    }

    override suspend fun saveWallets(wallets: List<WalletEntity>) {
        db.walletDao().insertAll(wallets)
    }

    override suspend fun markWalletDeleted(id: String, updatedAt: Long) {
        db.walletDao().markDeleted(id, updatedAt)
    }

    override suspend fun replaceAllWallets(wallets: List<WalletEntity>) {
        db.withTransaction {
            db.walletDao().deleteAll()
            db.walletDao().insertAll(wallets)
        }
    }

    // --- Kategori & Sub Kategori ---

    override fun getCategoriesFlow(): Flow<CategoriesData> {
        return combine(
            db.categoryDao().getCategoriesFlow(),
            db.categoryDao().getSubcategoriesFlow()
        ) { categories, subcategories ->
            if (categories.isEmpty()) {
                DefaultData.DEFAULT_CATEGORIES
            } else {
                val updatedAt = getMeta("categories_updated_at")?.toLongOrNull() ?: 0L
                buildCategoriesData(categories, subcategories, updatedAt)
            }
        }
    }

    override suspend fun getCategories(): CategoriesData {
        val categories = db.categoryDao().getCategories()
        if (categories.isEmpty()) {
            val def = DefaultData.DEFAULT_CATEGORIES
            saveCategories(def)
            return def
        }
        val subcategories = db.categoryDao().getSubcategories()
        val updatedAt = getMeta("categories_updated_at")?.toLongOrNull() ?: 0L
        return buildCategoriesData(categories, subcategories, updatedAt)
    }

    override suspend fun saveCategories(categories: CategoriesData) {
        db.withTransaction {
            db.categoryDao().deleteAllCategories()
            db.categoryDao().deleteAllSubcategories()

            val catEntities = ArrayList<CategoryEntity>()
            categories.income.forEach { catEntities.add(CategoryEntity("income", it.name, it.order, it.icon)) }
            categories.expense.forEach { catEntities.add(CategoryEntity("expense", it.name, it.order, it.icon)) }
            db.categoryDao().insertCategories(catEntities)

            val subEntities = ArrayList<SubcategoryEntity>()
            categories.subcategories.income.forEach { (parent, subs) ->
                subs.forEach { subEntities.add(SubcategoryEntity("income", parent, it.name, it.order, it.icon)) }
            }
            categories.subcategories.expense.forEach { (parent, subs) ->
                subs.forEach { subEntities.add(SubcategoryEntity("expense", parent, it.name, it.order, it.icon)) }
            }
            db.categoryDao().insertSubcategories(subEntities)

            db.metaDao().set(MetaEntity("categories_updated_at", categories.updatedAt.toString()))
        }
    }

    private fun buildCategoriesData(
        categories: List<CategoryEntity>,
        subcategories: List<SubcategoryEntity>,
        updatedAt: Long
    ): CategoriesData {
        val incomeCats = categories.filter { it.type == "income" }
            .sortedBy { it.order }
            .map { CategoryItem(it.name, it.order, it.icon) }
        val expenseCats = categories.filter { it.type == "expense" }
            .sortedBy { it.order }
            .map { CategoryItem(it.name, it.order, it.icon) }

        val incomeSubs = subcategories.filter { it.type == "income" }
            .groupBy { it.parentCategory }
            .mapValues { (_, subs) ->
                subs.sortedBy { it.order }.map { CategoryItem(it.name, it.order, it.icon) }
            }
        val expenseSubs = subcategories.filter { it.type == "expense" }
            .groupBy { it.parentCategory }
            .mapValues { (_, subs) ->
                subs.sortedBy { it.order }.map { CategoryItem(it.name, it.order, it.icon) }
            }

        return CategoriesData(
            income = incomeCats,
            expense = expenseCats,
            subcategories = SubcategoriesGroup(
                income = incomeSubs,
                expense = expenseSubs
            ),
            updatedAt = updatedAt
        )
    }

    // --- Anggaran (Budget) ---

    override fun getBudgetsFlow(): Flow<BudgetsData> {
        return combine(
            db.budgetDao().getBudgetsFlow(),
            db.budgetDao().getSubcategoryBudgetsFlow()
        ) { items, subcategories ->
            val updatedAt = getMeta("budgets_updated_at")?.toLongOrNull() ?: 0L
            buildBudgetsData(items, subcategories, updatedAt)
        }
    }

    override suspend fun getBudgets(): BudgetsData {
        val items = db.budgetDao().getBudgets()
        val subcategories = db.budgetDao().getSubcategoryBudgets()
        val updatedAt = getMeta("budgets_updated_at")?.toLongOrNull() ?: 0L
        return buildBudgetsData(items, subcategories, updatedAt)
    }

    override suspend fun saveBudgets(budgets: BudgetsData) {
        db.withTransaction {
            db.budgetDao().deleteAllBudgets()
            db.budgetDao().deleteAllSubcategoryBudgets()

            val budgetEntities = budgets.items.map { BudgetEntity(it.key, it.value) }
            db.budgetDao().insertBudgets(budgetEntities)

            val subEntities = ArrayList<SubcategoryBudgetEntity>()
            budgets.subcategories.forEach { (parent, subs) ->
                subs.forEach { (sub, amt) ->
                    subEntities.add(SubcategoryBudgetEntity(parent, sub, amt))
                }
            }
            db.budgetDao().insertSubcategoryBudgets(subEntities)

            db.metaDao().set(MetaEntity("budgets_updated_at", budgets.updatedAt.toString()))
        }
    }

    private fun buildBudgetsData(
        items: List<BudgetEntity>,
        subcategories: List<SubcategoryBudgetEntity>,
        updatedAt: Long
    ): BudgetsData {
        val itemsMap = items.associate { it.category to it.amount }
        val subsMap = subcategories.groupBy { it.category }
            .mapValues { (_, subs) ->
                subs.associate { it.subcategory to it.amount }
            }
        return BudgetsData(
            items = itemsMap,
            subcategories = subsMap,
            updatedAt = updatedAt
        )
    }

    // --- Pengaturan (Settings) ---

    override fun getSettingsFlow(): Flow<SettingsEntity> {
        return db.settingsDao().getSettingsFlow().map { it ?: DefaultData.DEFAULT_SETTINGS }
    }

    override suspend fun getSettings(): SettingsEntity {
        return db.settingsDao().getSettings() ?: DefaultData.DEFAULT_SETTINGS
    }

    override suspend fun saveSettings(settings: SettingsEntity) {
        db.settingsDao().insertOrUpdate(settings)
    }

    // --- Metadata ---

    override suspend fun getMeta(key: String): String? {
        return db.metaDao().get(key)
    }

    override suspend fun setMeta(key: String, value: String) {
        db.metaDao().set(MetaEntity(key, value))
    }

    override suspend fun deleteMeta(key: String) {
        db.metaDao().delete(key)
    }

    // --- Kembalikan ke bawaan (Factory Reset level data) ---

    override suspend fun resetToDefaults() {
        db.withTransaction {
            db.transactionDao().deleteAll()

            saveCategories(DefaultData.DEFAULT_CATEGORIES)
            db.metaDao().set(MetaEntity("categories_updated_at", "0"))

            saveBudgets(BudgetsData(updatedAt = 0L))
            db.metaDao().set(MetaEntity("budgets_updated_at", "0"))

            db.walletDao().deleteAll()
            val initialWallets = DefaultData.DEFAULT_WALLETS.map {
                it.copy(createdAt = System.currentTimeMillis(), updatedAt = 0L)
            }
            db.walletDao().insertAll(initialWallets)

            db.settingsDao().deleteAll()
            db.settingsDao().insertOrUpdate(DefaultData.DEFAULT_SETTINGS)

            db.metaDao().set(MetaEntity("localRev", "0"))
        }
    }
}
