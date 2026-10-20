package com.mizoar.mizanokane.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas Room untuk tabel dompet riil.
 * Sesuai dev/SCHEMA.md bagian 3 rincian field wallets.
 */
@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val currency: String,
    val order: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val deleted: Boolean = false
)
