package com.mizoar.mizanokane.domain

import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import java.util.Locale

data class RiwayatSummary(
    val targetCurrencies: List<String>,
    val incomeByCurrency: Map<String, Long>,
    val expenseByCurrency: Map<String, Long>
)

data class DateGroup(
    val dateKey: String,
    val title: String,
    val netByCurrency: Map<String, Long>,
    val transactions: List<TransactionEntity>
)

/**
 * Memeriksa apakah transaksi cocok dengan kueri pencarian riwayat
 * sesuai web (lines 5228-5325 desktop/src/js/app.js).
 */
fun matchesHistorySearch(
    tx: TransactionEntity,
    query: String,
    wallets: List<WalletEntity>
): Boolean {
    if (tx.deleted) return false
    val q = query.trim().lowercase(Locale.ROOT)
    if (q.isEmpty()) return true

    // 1. Catatan
    if (tx.note != null && tx.note.lowercase(Locale.ROOT).contains(q)) {
        return true
    }

    // 2. Kategori
    when (tx.type) {
        "transfer" -> {
            if ("transfer".contains(q)) return true
            if (tx.category != null && tx.category.lowercase(Locale.ROOT).contains(q)) return true
        }
        "penyesuaian" -> {
            if ("penyesuaian".contains(q)) return true
            if ("equilibrium".contains(q)) return true
            if (tx.category != null && tx.category.lowercase(Locale.ROOT).contains(q)) return true
        }
        else -> {
            if (tx.category != null && tx.category.lowercase(Locale.ROOT).contains(q)) return true
        }
    }

    // Teks gabungan kategori dan subkategori, misal "Makanan > Snack"
    if (!tx.category.isNullOrBlank() && !tx.subcategory.isNullOrBlank()) {
        val combined = "${tx.category} > ${tx.subcategory}".lowercase(Locale.ROOT)
        if (combined.contains(q)) return true
    }

    // 3. Sub kategori
    if (tx.subcategory != null && tx.subcategory.lowercase(Locale.ROOT).contains(q)) {
        return true
    }

    // 4. Nama dompet
    val walletMap = HashMap<String, String>()
    for (w in wallets) {
        walletMap[w.id] = w.name
    }
    val fromName = walletMap[tx.walletId ?: DefaultData.DEFAULT_WALLET_ID] ?: DefaultData.DEFAULT_WALLET_NAME
    if (fromName.lowercase(Locale.ROOT).contains(q)) {
        return true
    }
    if (tx.type == "transfer" && tx.toWalletId != null) {
        val toName = walletMap[tx.toWalletId] ?: ""
        if (toName.lowercase(Locale.ROOT).contains(q)) {
            return true
        }
    }

    // 5. Nominal
    val curr = Uang.getTransactionCurrency(tx, wallets)
    val rawAmountStr = tx.amount.toString()
    if (rawAmountStr.contains(q)) return true

    val formattedNum = Uang.formatNumber(tx.amount / 100)
    if (formattedNum.lowercase(Locale.ROOT).contains(q)) return true

    val formattedInput = Uang.formatAmountForInput(tx.amount, curr)
    if (formattedInput.lowercase(Locale.ROOT).contains(q)) return true

    val formattedCurrency = Uang.formatCurrency(tx.amount, curr)
    if (formattedCurrency.lowercase(Locale.ROOT).contains(q)) return true

    val cleanDigits = q.filter { it.isDigit() }
    if (cleanDigits.isNotEmpty() && rawAmountStr.contains(cleanDigits)) {
        return true
    }

    return false
}

/**
 * Menghitung ringkasan pemasukan dan pengeluaran pada Riwayat
 * sesuai web (lines 5327-5400 desktop/src/js/app.js).
 * Transfer tidak dihitung sebagai pemasukan atau pengeluaran.
 */
fun getRiwayatSummary(
    transactions: List<TransactionEntity>,
    monthFilter: String,
    walletFilter: String,
    activeCurrencies: List<String>,
    wallets: List<WalletEntity>,
    searchQuery: String
): RiwayatSummary {
    val incomeByCurrency = LinkedHashMap<String, Long>()
    val expenseByCurrency = LinkedHashMap<String, Long>()

    for (c in activeCurrencies) {
        incomeByCurrency[c] = 0L
        expenseByCurrency[c] = 0L
    }

    val walletMap = HashMap<String, WalletEntity>()
    for (w in wallets) {
        walletMap[w.id] = w
    }

    val targetCurrencies = mutableListOf<String>()
    if (walletFilter != "all") {
        val targetWallet = walletMap[walletFilter]
        val wCurr = targetWallet?.currency?.takeIf { it.isNotBlank() } ?: "IDR"
        targetCurrencies.add(wCurr)
        if (!incomeByCurrency.containsKey(wCurr)) {
            incomeByCurrency[wCurr] = 0L
            expenseByCurrency[wCurr] = 0L
        }
    } else {
        targetCurrencies.addAll(activeCurrencies)
    }

    for (tx in transactions) {
        if (tx.deleted || tx.amount <= 0L || tx.date.isBlank()) continue
        if (tx.type != "income" && tx.type != "expense") continue

        if (walletFilter != "all") {
            val wId = tx.walletId ?: DefaultData.DEFAULT_WALLET_ID
            if (wId != walletFilter) continue
        }

        if (monthFilter != "all") {
            val txMonth = tx.date.take(7)
            if (txMonth != monthFilter) continue
        }

        if (searchQuery.isNotBlank() && !matchesHistorySearch(tx, searchQuery, wallets)) {
            continue
        }

        val curr = Uang.getTransactionCurrency(tx, wallets)
        if (!targetCurrencies.contains(curr) && walletFilter == "all") {
            targetCurrencies.add(curr)
        }

        when (tx.type) {
            "income" -> {
                incomeByCurrency[curr] = (incomeByCurrency[curr] ?: 0L) + tx.amount
            }
            "expense" -> {
                expenseByCurrency[curr] = (expenseByCurrency[curr] ?: 0L) + tx.amount
            }
        }
    }

    return RiwayatSummary(
        targetCurrencies = targetCurrencies,
        incomeByCurrency = incomeByCurrency,
        expenseByCurrency = expenseByCurrency
    )
}

/**
 * Filter transaksi dan kelompokkan berdasarkan tanggal sesuai web (lines 5715-5841 desktop/src/js/app.js).
 */
fun filterAndGroupTransactions(
    transactions: List<TransactionEntity>,
    monthFilter: String,
    walletFilter: String,
    searchQuery: String,
    wallets: List<WalletEntity>,
    todayStr: String = getCurrentDateString()
): List<DateGroup> {
    val filtered = transactions.filter { tx ->
        if (tx.deleted) return@filter false

        if (monthFilter != "all") {
            val txMonth = tx.date.take(7)
            if (txMonth != monthFilter) return@filter false
        }

        if (walletFilter != "all") {
            if (tx.type == "transfer") {
                val fromId = tx.walletId ?: DefaultData.DEFAULT_WALLET_ID
                val toId = tx.toWalletId
                if (fromId != walletFilter && toId != walletFilter) return@filter false
            } else {
                val wId = tx.walletId ?: DefaultData.DEFAULT_WALLET_ID
                if (wId != walletFilter) return@filter false
            }
        }

        if (searchQuery.isNotBlank() && !matchesHistorySearch(tx, searchQuery, wallets)) {
            return@filter false
        }

        true
    }.sortedWith { a, b ->
        // Urutkan tanggal menurun, lalu waktu menurun, lalu createdAt menurun
        val dateComp = b.date.compareTo(a.date)
        if (dateComp != 0) return@sortedWith dateComp
        val timeA = a.time ?: ""
        val timeB = b.time ?: ""
        val timeComp = timeB.compareTo(timeA)
        if (timeComp != 0) return@sortedWith timeComp
        b.createdAt.compareTo(a.createdAt)
    }

    val dateGroupsMap = LinkedHashMap<String, MutableList<TransactionEntity>>()
    for (tx in filtered) {
        val dateKey = tx.date
        dateGroupsMap.getOrPut(dateKey) { mutableListOf() }.add(tx)
    }

    val result = mutableListOf<DateGroup>()
    for ((dateKey, groupTxList) in dateGroupsMap) {
        val netByCurrency = LinkedHashMap<String, Long>()

        for (tx in groupTxList) {
            val c = Uang.getTransactionCurrency(tx, wallets)
            val currentNet = netByCurrency[c] ?: 0L
            when (tx.type) {
                "income" -> {
                    netByCurrency[c] = currentNet + tx.amount
                }
                "expense" -> {
                    netByCurrency[c] = currentNet - tx.amount
                }
                "transfer" -> {
                    val fromId = tx.walletId ?: DefaultData.DEFAULT_WALLET_ID
                    val toId = tx.toWalletId
                    if (walletFilter == "all") {
                        // Transfer antar dompet satu mata uang tidak mengubah netto total
                    } else if (walletFilter == fromId) {
                        netByCurrency[c] = currentNet - tx.amount
                    } else if (walletFilter == toId) {
                        netByCurrency[c] = currentNet + tx.amount
                    }
                }
                "penyesuaian" -> {
                    val wId = tx.walletId ?: DefaultData.DEFAULT_WALLET_ID
                    if (walletFilter == "all" || walletFilter == wId) {
                        netByCurrency[c] = currentNet + tx.amount
                    }
                }
            }
        }

        result.add(
            DateGroup(
                dateKey = dateKey,
                title = formatGroupDate(dateKey, todayStr),
                netByCurrency = netByCurrency,
                transactions = groupTxList
            )
        )
    }

    return result
}
