package com.mizoar.mizanokane.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mizoar.mizanokane.data.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object untuk operasi tabel pengaturan (settings).
 */
@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1 LIMIT 1")
    fun getSettingsFlow(): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: SettingsEntity)

    @Query("DELETE FROM settings")
    suspend fun deleteAll()
}
