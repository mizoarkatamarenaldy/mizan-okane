package com.mizoar.mizanokane.data.model

/**
 * Struktur utuh objek anggaran (budget) sesuai dev/SCHEMA.md bagian 3 rincian field budgets.
 *
 * Catatan satuan:
 * Nilai uang di [items] dan [subcategories] disimpan dalam satuan seperseratus (1/100 unit mata uang / cents).
 */
data class BudgetsData(
    val items: Map<String, Long> = emptyMap(),
    val subcategories: Map<String, Map<String, Long>> = emptyMap(),
    val updatedAt: Long = 0L
)
