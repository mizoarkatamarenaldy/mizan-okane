package com.mizoar.mizanokane.domain

import com.mizoar.mizanokane.data.model.TransactionEntity
import java.util.Locale

data class NoteSuggestion(
    val text: String,
    val count: Int,
    val startsWith: Boolean
)

/**
 * Pembanding kebaruan transaksi sesuai web (lines 1282-1293 desktop/src/js/app.js).
 * Mengembalikan angka positif bila txA lebih baru daripada txB.
 */
fun compareTxRecency(txA: TransactionEntity?, txB: TransactionEntity?): Int {
    if (txA == null && txB == null) return 0
    if (txA == null) return -1
    if (txB == null) return 1

    val dateA = txA.date
    val dateB = txB.date
    if (dateA != dateB) return dateA.compareTo(dateB)

    val timeA = txA.time ?: ""
    val timeB = txB.time ?: ""
    if (timeA != timeB) return timeA.compareTo(timeB)

    return txA.createdAt.compareTo(txB.createdAt)
}

/**
 * Logika pencarian saran catatan lama sesuai spek dan web (lines 1295-1377 desktop/src/js/app.js).
 */
fun getNoteSuggestions(transactions: List<TransactionEntity>, query: String): List<NoteSuggestion> {
    val typed = query.trim()
    if (typed.length < 2) return emptyList()
    val lowerTyped = typed.lowercase(Locale.ROOT)

    // 1. Kumpulkan catatan riwayat yang valid (tidak terhapus, bukan transfer/penyesuaian/equilibrium)
    data class NoteData(var text: String, var count: Int, var latestTx: TransactionEntity)
    val noteMap = LinkedHashMap<String, NoteData>()

    for (tx in transactions) {
        if (tx.deleted) continue
        if (tx.type == "transfer" || tx.type == "penyesuaian") continue
        if ((tx.category ?: "").trim().lowercase(Locale.ROOT) == "equilibrium") continue
        val note = tx.note ?: continue
        val trimmed = note.trim()
        if (trimmed.isEmpty()) continue
        val key = trimmed.lowercase(Locale.ROOT)

        val existing = noteMap[key]
        if (existing == null) {
            noteMap[key] = NoteData(trimmed, 1, tx)
        } else {
            existing.count += 1
            if (compareTxRecency(tx, existing.latestTx) > 0) {
                existing.latestTx = tx
                existing.text = trimmed
            }
        }
    }

    // 2. Filter catatan yang mengandung query
    data class MatchedNote(val text: String, val count: Int, val latestTx: TransactionEntity, val startsWith: Boolean)
    val matched = mutableListOf<MatchedNote>()

    for (item in noteMap.values) {
        val lowerText = item.text.lowercase(Locale.ROOT)
        val index = lowerText.indexOf(lowerTyped)
        if (index != -1) {
            matched.add(
                MatchedNote(
                    text = item.text,
                    count = item.count,
                    latestTx = item.latestTx,
                    startsWith = (index == 0)
                )
            )
        }
    }

    // 3. Urutkan:
    // - yang diawali ketikan dulu
    // - lalu yang paling sering dipakai
    // - lalu yang paling baru dipakai
    // - lalu alfabetis
    matched.sortWith { a, b ->
        if (a.startsWith != b.startsWith) {
            if (a.startsWith) -1 else 1
        } else if (a.count != b.count) {
            b.count.compareTo(a.count)
        } else {
            val recencyComp = compareTxRecency(a.latestTx, b.latestTx)
            if (recencyComp != 0) {
                -recencyComp
            } else {
                a.text.compareTo(b.text)
            }
        }
    }

    // 4. Batasi maksimal 8 saran
    val top = matched.take(8)

    // 5. Jika tidak ada atau hanya 1 dan teksnya sama persis dengan yang diketik, jangan tampilkan
    if (top.isEmpty()) return emptyList()
    if (top.size == 1 && top[0].text.trim().lowercase(Locale.ROOT) == lowerTyped) {
        return emptyList()
    }

    return top.map { NoteSuggestion(text = it.text, count = it.count, startsWith = it.startsWith) }
}
