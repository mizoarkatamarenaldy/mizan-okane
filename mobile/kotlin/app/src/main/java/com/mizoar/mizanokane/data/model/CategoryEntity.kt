package com.mizoar.mizanokane.data.model

import androidx.room.Entity

/**
 * Entitas Room untuk kategori induk (pemasukan atau pengeluaran).
 * Sesuai dev/SCHEMA.md bagian 3 rincian field categories.
 */
@Entity(tableName = "categories", primaryKeys = ["type", "name"])
data class CategoryEntity(
    val type: String, // "income" atau "expense"
    val name: String,
    val order: Int,
    val icon: String? = null
)
