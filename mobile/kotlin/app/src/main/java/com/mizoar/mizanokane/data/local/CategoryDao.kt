package com.mizoar.mizanokane.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mizoar.mizanokane.data.model.CategoryEntity
import com.mizoar.mizanokane.data.model.SubcategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object untuk operasi tabel kategori dan sub kategori.
 */
@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY `order` ASC")
    fun getCategoriesFlow(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories ORDER BY `order` ASC")
    suspend fun getCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM subcategories ORDER BY `order` ASC")
    fun getSubcategoriesFlow(): Flow<List<SubcategoryEntity>>

    @Query("SELECT * FROM subcategories ORDER BY `order` ASC")
    suspend fun getSubcategories(): List<SubcategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubcategories(subcategories: List<SubcategoryEntity>)

    @Query("DELETE FROM subcategories")
    suspend fun deleteAllSubcategories()
}
