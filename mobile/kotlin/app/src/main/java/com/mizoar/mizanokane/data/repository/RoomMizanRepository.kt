package com.mizoar.mizanokane.data.repository

import androidx.room.withTransaction
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.SampleData
import com.mizoar.mizanokane.data.json.BudgetDto
import com.mizoar.mizanokane.data.json.JsonBackupManager
import com.mizoar.mizanokane.data.json.TransactionDto
import com.mizoar.mizanokane.data.json.WalletDto
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
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer

/**
 * Implementasi [MizanRepository] berbasis Room Database.
 */
class RoomMizanRepository(private val db: AppDatabase) : MizanRepository {

    // --- Transaksi ---

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllTransactionsFlow(): Flow<List<TransactionEntity>> {
        return db.metaDao().getFlow("demo_mode").flatMapLatest { demoVal ->
            if (demoVal == "true") {
                db.metaDao().getFlow("demo_transactions").map { jsonStr ->
                    if (jsonStr.isNullOrBlank()) emptyList()
                    else deserializeDemoTransactions(jsonStr)
                }
            } else {
                db.transactionDao().getAllFlow()
            }
        }
    }

    override suspend fun getAllTransactions(): List<TransactionEntity> {
        if (isDemoMode()) {
            val jsonStr = db.metaDao().get("demo_transactions")
            return if (jsonStr.isNullOrBlank()) emptyList() else deserializeDemoTransactions(jsonStr)
        }
        return db.transactionDao().getAll()
    }

    override suspend fun getTransactionById(id: String): TransactionEntity? {
        if (isDemoMode()) {
            return getAllTransactions().find { it.id == id }
        }
        return db.transactionDao().getById(id)
    }

    override suspend fun saveTransaction(tx: TransactionEntity) {
        if (isDemoMode()) {
            val list = getAllTransactions().toMutableList()
            val idx = list.indexOfFirst { it.id == tx.id }
            if (idx >= 0) list[idx] = tx else list.add(tx)
            val jsonStr = JsonBackupManager.json.encodeToString(
                ListSerializer(TransactionDto.serializer()),
                list.map { TransactionDto.fromEntity(it) }
            )
            db.metaDao().set(MetaEntity("demo_transactions", jsonStr))
        } else {
            db.transactionDao().insert(tx)
        }
    }

    override suspend fun markTransactionDeleted(id: String, updatedAt: Long) {
        if (isDemoMode()) {
            val list = getAllTransactions().toMutableList()
            val idx = list.indexOfFirst { it.id == id }
            if (idx >= 0) {
                list[idx] = list[idx].copy(deleted = true, updatedAt = updatedAt)
                val jsonStr = JsonBackupManager.json.encodeToString(
                    ListSerializer(TransactionDto.serializer()),
                    list.map { TransactionDto.fromEntity(it) }
                )
                db.metaDao().set(MetaEntity("demo_transactions", jsonStr))
            }
        } else {
            db.transactionDao().markDeleted(id, updatedAt)
        }
    }

    override suspend fun replaceAllTransactions(transactions: List<TransactionEntity>) {
        if (isDemoMode()) {
            val jsonStr = JsonBackupManager.json.encodeToString(
                ListSerializer(TransactionDto.serializer()),
                transactions.map { TransactionDto.fromEntity(it) }
            )
            db.metaDao().set(MetaEntity("demo_transactions", jsonStr))
        } else {
            db.withTransaction {
                db.transactionDao().deleteAll()
                db.transactionDao().insertAll(transactions)
            }
        }
    }

    // --- Dompet ---

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllWalletsFlow(): Flow<List<WalletEntity>> {
        return db.metaDao().getFlow("demo_mode").flatMapLatest { demoVal ->
            if (demoVal == "true") {
                db.metaDao().getFlow("demo_wallets").map { jsonStr ->
                    if (jsonStr.isNullOrBlank()) SampleData.SAMPLE_WALLETS
                    else deserializeDemoWallets(jsonStr)
                }
            } else {
                getRealWalletsFlow()
            }
        }
    }

    private fun getRealWalletsFlow(): Flow<List<WalletEntity>> {
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
        if (isDemoMode()) {
            val jsonStr = db.metaDao().get("demo_wallets")
            return if (jsonStr.isNullOrBlank()) SampleData.SAMPLE_WALLETS else deserializeDemoWallets(jsonStr)
        }
        return getRealWallets()
    }

    override suspend fun getWalletById(id: String): WalletEntity? {
        if (isDemoMode()) {
            return getAllWallets().find { it.id == id }
        }
        return db.walletDao().getById(id)
    }

    override suspend fun saveWallet(wallet: WalletEntity) {
        if (isDemoMode()) {
            val list = getAllWallets().toMutableList()
            val idx = list.indexOfFirst { it.id == wallet.id }
            if (idx >= 0) list[idx] = wallet else list.add(wallet)
            val jsonStr = JsonBackupManager.json.encodeToString(
                ListSerializer(WalletDto.serializer()),
                list.map { WalletDto.fromEntity(it) }
            )
            db.metaDao().set(MetaEntity("demo_wallets", jsonStr))
        } else {
            db.walletDao().insert(wallet)
        }
    }

    override suspend fun saveWallets(wallets: List<WalletEntity>) {
        if (isDemoMode()) {
            val jsonStr = JsonBackupManager.json.encodeToString(
                ListSerializer(WalletDto.serializer()),
                wallets.map { WalletDto.fromEntity(it) }
            )
            db.metaDao().set(MetaEntity("demo_wallets", jsonStr))
        } else {
            db.walletDao().insertAll(wallets)
        }
    }

    override suspend fun markWalletDeleted(id: String, updatedAt: Long) {
        if (isDemoMode()) {
            val list = getAllWallets().toMutableList()
            val idx = list.indexOfFirst { it.id == id }
            if (idx >= 0) {
                list[idx] = list[idx].copy(deleted = true, updatedAt = updatedAt)
                val jsonStr = JsonBackupManager.json.encodeToString(
                    ListSerializer(WalletDto.serializer()),
                    list.map { WalletDto.fromEntity(it) }
                )
                db.metaDao().set(MetaEntity("demo_wallets", jsonStr))
            }
        } else {
            db.walletDao().markDeleted(id, updatedAt)
        }
    }

    override suspend fun replaceAllWallets(wallets: List<WalletEntity>) {
        if (isDemoMode()) {
            val jsonStr = JsonBackupManager.json.encodeToString(
                ListSerializer(WalletDto.serializer()),
                wallets.map { WalletDto.fromEntity(it) }
            )
            db.metaDao().set(MetaEntity("demo_wallets", jsonStr))
        } else {
            db.withTransaction {
                db.walletDao().deleteAll()
                db.walletDao().insertAll(wallets)
            }
        }
    }

    // --- Kategori & Sub Kategori ---

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCategoriesFlow(): Flow<CategoriesData> {
        return db.metaDao().getFlow("demo_mode").flatMapLatest { demoVal ->
            if (demoVal == "true") {
                db.metaDao().getFlow("demo_categories").map { jsonStr ->
                    if (jsonStr.isNullOrBlank()) SampleData.SAMPLE_CATEGORIES
                    else deserializeDemoCategories(jsonStr)
                }
            } else {
                getRealCategoriesFlow()
            }
        }
    }

    private fun getRealCategoriesFlow(): Flow<CategoriesData> {
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
        if (isDemoMode()) {
            val jsonStr = db.metaDao().get("demo_categories")
            return if (jsonStr.isNullOrBlank()) SampleData.SAMPLE_CATEGORIES else deserializeDemoCategories(jsonStr)
        }
        return getRealCategories()
    }

    override suspend fun saveCategories(categories: CategoriesData) {
        if (isDemoMode()) {
            val jsonStr = JsonBackupManager.json.encodeToString(
                CategoriesData.serializer(),
                categories
            )
            db.metaDao().set(MetaEntity("demo_categories", jsonStr))
        } else {
            saveRealCategories(categories)
        }
    }

    private suspend fun saveRealCategories(categories: CategoriesData) {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBudgetsFlow(): Flow<BudgetsData> {
        return db.metaDao().getFlow("demo_mode").flatMapLatest { demoVal ->
            if (demoVal == "true") {
                db.metaDao().getFlow("demo_budgets").map { jsonStr ->
                    if (jsonStr.isNullOrBlank()) SampleData.SAMPLE_BUDGETS
                    else deserializeDemoBudgets(jsonStr)
                }
            } else {
                getRealBudgetsFlow()
            }
        }
    }

    private fun getRealBudgetsFlow(): Flow<BudgetsData> {
        return combine(
            db.budgetDao().getBudgetsFlow(),
            db.budgetDao().getSubcategoryBudgetsFlow()
        ) { items, subcategories ->
            val updatedAt = getMeta("budgets_updated_at")?.toLongOrNull() ?: 0L
            buildBudgetsData(items, subcategories, updatedAt)
        }
    }

    override suspend fun getBudgets(): BudgetsData {
        if (isDemoMode()) {
            val jsonStr = db.metaDao().get("demo_budgets")
            return if (jsonStr.isNullOrBlank()) SampleData.SAMPLE_BUDGETS else deserializeDemoBudgets(jsonStr)
        }
        return getRealBudgets()
    }

    override suspend fun saveBudgets(budgets: BudgetsData) {
        if (isDemoMode()) {
            val jsonStr = JsonBackupManager.json.encodeToString(
                BudgetDto.serializer(),
                BudgetDto.fromBudgetsData(budgets)
            )
            db.metaDao().set(MetaEntity("demo_budgets", jsonStr))
        } else {
            saveRealBudgets(budgets)
        }
    }

    private suspend fun saveRealBudgets(budgets: BudgetsData) {
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

    // --- Mode Data Contoh (Demo) & Data Asli ---

    override fun isDemoModeFlow(): Flow<Boolean> {
        return db.metaDao().getFlow("demo_mode").map { it == "true" }
    }

    override suspend fun isDemoMode(): Boolean {
        return db.metaDao().get("demo_mode") == "true"
    }

    override suspend fun activateSampleData(referenceDate: LocalDate) {
        db.withTransaction {
            val walletsJson = JsonBackupManager.json.encodeToString(
                ListSerializer(WalletDto.serializer()),
                SampleData.SAMPLE_WALLETS.map { WalletDto.fromEntity(it) }
            )
            val catsJson = JsonBackupManager.json.encodeToString(
                CategoriesData.serializer(),
                SampleData.SAMPLE_CATEGORIES
            )
            val budgetsJson = JsonBackupManager.json.encodeToString(
                BudgetDto.serializer(),
                BudgetDto.fromBudgetsData(SampleData.SAMPLE_BUDGETS)
            )
            val txsJson = JsonBackupManager.json.encodeToString(
                ListSerializer(TransactionDto.serializer()),
                SampleData.getSampleTransactions(referenceDate).map { TransactionDto.fromEntity(it) }
            )

            db.metaDao().set(MetaEntity("demo_wallets", walletsJson))
            db.metaDao().set(MetaEntity("demo_categories", catsJson))
            db.metaDao().set(MetaEntity("demo_budgets", budgetsJson))
            db.metaDao().set(MetaEntity("demo_transactions", txsJson))
            db.metaDao().set(MetaEntity("demo_mode", "true"))
        }
    }

    override suspend fun clearSampleData() {
        db.withTransaction {
            db.metaDao().delete("demo_wallets")
            db.metaDao().delete("demo_categories")
            db.metaDao().delete("demo_budgets")
            db.metaDao().delete("demo_transactions")
            db.metaDao().delete("demo_mode")
        }
    }

    override suspend fun getRealTransactions(): List<TransactionEntity> {
        return db.transactionDao().getAll()
    }

    override suspend fun getRealWallets(): List<WalletEntity> {
        val list = db.walletDao().getAll()
        return if (list.isEmpty()) {
            val initial = DefaultData.DEFAULT_WALLETS.map { it.copy(createdAt = System.currentTimeMillis()) }
            db.walletDao().insertAll(initial)
            initial
        } else {
            list
        }
    }

    override suspend fun getRealCategories(): CategoriesData {
        val categories = db.categoryDao().getCategories()
        if (categories.isEmpty()) {
            val def = DefaultData.DEFAULT_CATEGORIES
            saveRealCategories(def)
            return def
        }
        val subcategories = db.categoryDao().getSubcategories()
        val updatedAt = getMeta("categories_updated_at")?.toLongOrNull() ?: 0L
        return buildCategoriesData(categories, subcategories, updatedAt)
    }

    override suspend fun getRealBudgets(): BudgetsData {
        val items = db.budgetDao().getBudgets()
        val subcategories = db.budgetDao().getSubcategoryBudgets()
        val updatedAt = getMeta("budgets_updated_at")?.toLongOrNull() ?: 0L
        return buildBudgetsData(items, subcategories, updatedAt)
    }

    private fun deserializeDemoTransactions(jsonStr: String): List<TransactionEntity> {
        return try {
            val dtoList = JsonBackupManager.json.decodeFromString(
                ListSerializer(TransactionDto.serializer()),
                jsonStr
            )
            dtoList.map { it.toEntity() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun deserializeDemoWallets(jsonStr: String): List<WalletEntity> {
        return try {
            val dtoList = JsonBackupManager.json.decodeFromString(
                ListSerializer(WalletDto.serializer()),
                jsonStr
            )
            dtoList.map { it.toEntity() }.sortedBy { it.order }
        } catch (e: Exception) {
            SampleData.SAMPLE_WALLETS
        }
    }

    private fun deserializeDemoCategories(jsonStr: String): CategoriesData {
        return try {
            JsonBackupManager.json.decodeFromString(
                CategoriesData.serializer(),
                jsonStr
            )
        } catch (e: Exception) {
            SampleData.SAMPLE_CATEGORIES
        }
    }

    private fun deserializeDemoBudgets(jsonStr: String): BudgetsData {
        return try {
            val dto = JsonBackupManager.json.decodeFromString(
                BudgetDto.serializer(),
                jsonStr
            )
            dto.toBudgetsData()
        } catch (e: Exception) {
            SampleData.SAMPLE_BUDGETS
        }
    }

    // --- Kembalikan ke bawaan (Factory Reset level data) ---

    override suspend fun resetToDefaults() {
        db.withTransaction {
            db.transactionDao().deleteAll()

            saveRealCategories(DefaultData.DEFAULT_CATEGORIES)
            db.metaDao().set(MetaEntity("categories_updated_at", "0"))

            saveRealBudgets(BudgetsData(updatedAt = 0L))
            db.metaDao().set(MetaEntity("budgets_updated_at", "0"))

            db.walletDao().deleteAll()
            val initialWallets = DefaultData.DEFAULT_WALLETS.map {
                it.copy(createdAt = System.currentTimeMillis(), updatedAt = 0L)
            }
            db.walletDao().insertAll(initialWallets)

            db.settingsDao().deleteAll()
            db.settingsDao().insertOrUpdate(DefaultData.DEFAULT_SETTINGS)

            db.metaDao().set(MetaEntity("localRev", "0"))

            // Bersihkan data demo jika ada
            db.metaDao().delete("demo_wallets")
            db.metaDao().delete("demo_categories")
            db.metaDao().delete("demo_budgets")
            db.metaDao().delete("demo_transactions")
            db.metaDao().delete("demo_mode")
        }
    }
}
