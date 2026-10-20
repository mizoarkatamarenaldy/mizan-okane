package com.mizoar.mizanokane.data.model

import androidx.room.Entity

/**
 * Entitas Room untuk batas anggaran sub kategori pengeluaran.
 * Sesuai dev/SCHEMA.md bagian 3 rincian field budgets.subcategories.
 *
 * [amount] disimpan sebagai [Long] dalam satuan seperseratus (1/100 unit mata uang).
 */
@Entity(tableName = "subcategory_budgets", primaryKeys = ["category", "subcategory"])
data class SubcategoryBudgetEntity(
    val category: String,
    val subcategory: String,
    val amount: Long
)
