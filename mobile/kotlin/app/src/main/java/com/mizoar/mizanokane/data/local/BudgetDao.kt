package com.mizoar.mizanokane.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mizoar.mizanokane.data.model.BudgetEntity
import com.mizoar.mizanokane.data.model.SubcategoryBudgetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object untuk operasi tabel anggaran (budget).
 */
@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets")
    fun getBudgetsFlow(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets")
    suspend fun getBudgets(): List<BudgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgets(budgets: List<BudgetEntity>)

    @Query("DELETE FROM budgets")
    suspend fun deleteAllBudgets()

    @Query("SELECT * FROM subcategory_budgets")
    fun getSubcategoryBudgetsFlow(): Flow<List<SubcategoryBudgetEntity>>

    @Query("SELECT * FROM subcategory_budgets")
    suspend fun getSubcategoryBudgets(): List<SubcategoryBudgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubcategoryBudgets(budgets: List<SubcategoryBudgetEntity>)

    @Query("DELETE FROM subcategory_budgets")
    suspend fun deleteAllSubcategoryBudgets()
}
