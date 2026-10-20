package com.mizoar.mizanokane.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mizoar.mizanokane.data.model.WalletEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object untuk operasi dasar tabel dompet.
 */
@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets ORDER BY `order` ASC")
    fun getAllFlow(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets ORDER BY `order` ASC")
    suspend fun getAll(): List<WalletEntity>

    @Query("SELECT * FROM wallets WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): WalletEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: WalletEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wallets: List<WalletEntity>)

    @Update
    suspend fun update(wallet: WalletEntity)

    @Query("UPDATE wallets SET deleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun markDeleted(id: String, updatedAt: Long)

    @Query("DELETE FROM wallets")
    suspend fun deleteAll()
}
