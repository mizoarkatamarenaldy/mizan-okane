package com.mizoar.mizanokane.domain

import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity

/**
 * Pembuat berkas CSV transaksi aplikasi Mizan Okane sesuai desktop/src/js/app.js.
 * Semua format angka uang diolah lewat object [Uang].
 */
object CsvExporter {

    fun generateCsv(
        transactions: List<TransactionEntity>,
        wallets: List<WalletEntity>
    ): String {
        val activeTx = transactions.filter { !it.deleted && !it.dummy }
        if (activeTx.isEmpty()) {
            return ""
        }

        val walletMap = wallets.associate { it.id to it.name }
        fun getWalletName(id: String?): String = walletMap[id] ?: ""

        val sortedTx = activeTx.sortedWith(
            compareByDescending<TransactionEntity> { it.date }
                .thenByDescending { it.time.orEmpty() }
                .thenByDescending { it.createdAt }
        )

        val headers = listOf("tanggal", "jam", "dompet", "tipe", "kategori", "jumlah", "catatan", "subkategori")
        val rows = ArrayList<String>()
        rows.add(headers.joinToString(","))

        for (tx in sortedTx) {
            val walletName = if (tx.type == "transfer") {
                "${getWalletName(tx.walletId)} -> ${getWalletName(tx.toWalletId)}"
            } else {
                getWalletName(tx.walletId)
            }
            val tipe = if (tx.type == "penyesuaian") "Penyesuaian" else tx.type
            val kategori = if (tx.type == "penyesuaian") {
                "Equilibrium"
            } else {
                tx.category ?: if (tx.type == "transfer") "Transfer" else ""
            }
            val jumlah = Uang.kePlainString(tx.amount)
            val catatan = tx.note.orEmpty()
            val subkategori = tx.subcategory.orEmpty()

            val row = listOf(
                escapeCsvField(tx.date),
                escapeCsvField(tx.time.orEmpty()),
                escapeCsvField(walletName),
                escapeCsvField(tipe),
                escapeCsvField(kategori),
                escapeCsvField(jumlah),
                escapeCsvField(catatan),
                escapeCsvField(subkategori)
            )
            rows.add(row.joinToString(","))
        }

        val csvContent = rows.joinToString("\r\n")
        val bom = "\uFEFF"
        return bom + csvContent
    }

    fun escapeCsvField(value: String): String {
        if (value.contains('"') || value.contains(',') || value.contains('\n') || value.contains('\r')) {
            return "\"${value.replace("\"", "\"\"")}\""
        }
        return value
    }
}
