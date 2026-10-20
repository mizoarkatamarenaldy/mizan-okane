package com.mizoar.mizanokane.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mizoar.mizanokane.data.model.MetaEntity

/**
 * Data Access Object untuk operasi tabel metadata kunci-nilai.
 */
@Dao
interface MetaDao {
    @Query("SELECT value FROM meta WHERE `key` = :key LIMIT 1")
    suspend fun get(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun set(meta: MetaEntity)

    @Query("DELETE FROM meta WHERE `key` = :key")
    suspend fun delete(key: String)

    @Query("DELETE FROM meta")
    suspend fun deleteAll()
}
