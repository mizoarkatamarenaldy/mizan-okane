package com.mizoar.mizanokane.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas Room untuk metadata pasangan kunci-nilai (seperti updatedAt kategori/budget, localRev).
 * Sesuai dev/SCHEMA.md bagian 2.B (Store meta).
 */
@Entity(tableName = "meta")
data class MetaEntity(
    @PrimaryKey
    val key: String,
    val value: String
)
