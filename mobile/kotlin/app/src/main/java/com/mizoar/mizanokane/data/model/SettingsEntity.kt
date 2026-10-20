package com.mizoar.mizanokane.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas Room untuk pengaturan antarmuka aplikasi.
 * Sesuai dev/SCHEMA.md bagian 3 rincian field settings.
 */
@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val theme: String = "system",
    val timeFormat: String = "24",
    val updatedAt: Long = 0L
)
