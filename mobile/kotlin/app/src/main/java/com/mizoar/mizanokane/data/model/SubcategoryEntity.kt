package com.mizoar.mizanokane.data.model

import androidx.room.Entity

/**
 * Entitas Room untuk sub kategori.
 * Sesuai dev/SCHEMA.md bagian 3 rincian field categories.subcategories.
 */
@Entity(tableName = "subcategories", primaryKeys = ["type", "parentCategory", "name"])
data class SubcategoryEntity(
    val type: String, // "income" atau "expense"
    val parentCategory: String,
    val name: String,
    val order: Int,
    val icon: String? = null
)
