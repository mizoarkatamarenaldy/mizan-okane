package com.mizoar.mizanokane.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas Room untuk tabel transaksi lokal (aktif maupun soft-deleted).
 * Sesuai dev/SCHEMA.md bagian 2.A (Store transactions).
 *
 * Catatan tipe data:
 * - [amount] disimpan sebagai [Long] dalam satuan seperseratus (1/100 unit mata uang / cents).
 *   Positif untuk transaksi biasa ('income', 'expense', 'transfer'),
 *   tidak nol untuk 'penyesuaian' (bisa negatif atau positif).
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val amount: Long,
    val date: String,
    val time: String? = null,
    val category: String? = null,
    val subcategory: String? = null,
    val walletId: String? = null,
    val toWalletId: String? = null,
    val note: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val deleted: Boolean = false,
    val dummy: Boolean = false
)
