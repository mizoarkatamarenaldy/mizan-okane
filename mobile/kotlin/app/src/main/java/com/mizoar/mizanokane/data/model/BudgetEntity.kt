package com.mizoar.mizanokane.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas Room untuk batas anggaran kategori pengeluaran induk.
 * Sesuai dev/SCHEMA.md bagian 3 rincian field budgets.items.
 *
 * [amount] disimpan sebagai [Long] dalam satuan seperseratus (1/100 unit mata uang).
 */
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey
    val category: String,
    val amount: Long
)
