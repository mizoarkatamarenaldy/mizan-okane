package com.mizoar.mizanokane.domain

import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity

data class BerandaSummary(
    val totalBalanceByCurrency: Map<String, Long>,
    val incomeByCurrency: Map<String, Long>,
    val expenseByCurrency: Map<String, Long>
)

data class SubcategoryBudgetItemSummary(
    val subcategoryName: String,
    val budgetAmount: Long,
    val terpakaiAmount: Long,
    val percent: Int,
    val isOver: Boolean,
    val sisaAmount: Long,
    val lebihAmount: Long
)

data class BudgetItemSummary(
    val categoryName: String,
    val budgetAmount: Long,
    val terpakaiAmount: Long,
    val percent: Int,
    val isOver: Boolean,
    val sisaAmount: Long,
    val lebihAmount: Long,
    val subcategories: List<SubcategoryBudgetItemSummary> = emptyList()
)

data class BudgetSectionSummary(
    val items: List<BudgetItemSummary>,
    val totalBudget: Long,
    val totalTerpakai: Long
)

fun getActiveWallets(wallets: List<WalletEntity>): List<WalletEntity> {
    return wallets.filter { !it.deleted }.sortedBy { it.order }
}

fun getActiveCurrencies(wallets: List<WalletEntity>): List<String> {
    val active = getActiveWallets(wallets)
    val list = mutableListOf<String>()
    if (active.any { it.currency.isBlank() || it.currency == "IDR" }) list.add("IDR")
    if (active.any { it.currency == "JPY" }) list.add("JPY")
    if (active.any { it.currency == "USD" }) list.add("USD")
    if (list.isEmpty()) list.add("IDR")
    return list
}

/**
 * Menghitung ringkasan Beranda (Total Saldo posisi akhir bulan, Pemasukan bulan berjalan,
 * Pengeluaran bulan berjalan) per mata uang sesuai web (lines 5145-5226 desktop/src/js/app.js).
 */
fun getBerandaMonthSummary(
    transactions: List<TransactionEntity>,
    targetMonthKey: String,
    activeCurrencies: List<String>,
    wallets: List<WalletEntity>
): BerandaSummary {
    val totalBalanceByCurrency = LinkedHashMap<String, Long>()
    val incomeByCurrency = LinkedHashMap<String, Long>()
    val expenseByCurrency = LinkedHashMap<String, Long>()

    for (c in activeCurrencies) {
        totalBalanceByCurrency[c] = 0L
        incomeByCurrency[c] = 0L
        expenseByCurrency[c] = 0L
    }

    val walletDeletedMap = HashMap<String, Boolean>()
    for (w in wallets) {
        walletDeletedMap[w.id] = w.deleted
    }
    fun isWalletActive(id: String?): Boolean {
        val wId = id ?: DefaultData.DEFAULT_WALLET_ID
        return if (!walletDeletedMap.containsKey(wId)) {
            wId == DefaultData.DEFAULT_WALLET_ID
        } else {
            walletDeletedMap[wId] == false
        }
    }

    for (tx in transactions) {
        if (tx.deleted || tx.amount == 0L || tx.date.isBlank()) continue
        val txMonth = tx.date.take(7) // "YYYY-MM"
        val curr = Uang.getTransactionCurrency(tx, wallets)

        if (txMonth <= targetMonthKey) {
            val fromId = tx.walletId ?: DefaultData.DEFAULT_WALLET_ID
            val fromActive = isWalletActive(fromId)

            when (tx.type) {
                "income" -> {
                    if (fromActive) {
                        totalBalanceByCurrency[curr] = (totalBalanceByCurrency[curr] ?: 0L) + tx.amount
                    }
                }
                "expense" -> {
                    if (fromActive) {
                        totalBalanceByCurrency[curr] = (totalBalanceByCurrency[curr] ?: 0L) - tx.amount
                    }
                }
                "transfer" -> {
                    val toId = tx.toWalletId
                    val toActive = isWalletActive(toId)
                    if (fromActive && !toActive) {
                        totalBalanceByCurrency[curr] = (totalBalanceByCurrency[curr] ?: 0L) - tx.amount
                    } else if (!fromActive && toActive) {
                        totalBalanceByCurrency[curr] = (totalBalanceByCurrency[curr] ?: 0L) + tx.amount
                    }
                }
                "penyesuaian" -> {
                    if (fromActive) {
                        totalBalanceByCurrency[curr] = (totalBalanceByCurrency[curr] ?: 0L) + tx.amount
                    }
                }
            }
        }

        if (txMonth == targetMonthKey) {
            when (tx.type) {
                "income" -> {
                    incomeByCurrency[curr] = (incomeByCurrency[curr] ?: 0L) + tx.amount
                }
                "expense" -> {
                    expenseByCurrency[curr] = (expenseByCurrency[curr] ?: 0L) + tx.amount
                }
            }
        }
    }

    return BerandaSummary(
        totalBalanceByCurrency = totalBalanceByCurrency,
        incomeByCurrency = incomeByCurrency,
        expenseByCurrency = expenseByCurrency
    )
}

/**
 * Menghitung saldo masing-masing dompet riil sesuai web (lines 5658-5681 desktop/src/js/app.js).
 */
fun calculateWalletBalances(
    transactions: List<TransactionEntity>,
    wallets: List<WalletEntity>
): Map<String, Long> {
    val activeWallets = getActiveWallets(wallets)
    val map = LinkedHashMap<String, Long>()
    for (w in activeWallets) {
        map[w.id] = 0L
    }

    for (tx in transactions) {
        if (tx.deleted) continue
        val wId = tx.walletId ?: DefaultData.DEFAULT_WALLET_ID
        when (tx.type) {
            "income" -> {
                map[wId] = (map[wId] ?: 0L) + tx.amount
            }
            "expense" -> {
                map[wId] = (map[wId] ?: 0L) - tx.amount
            }
            "transfer" -> {
                val fromId = tx.walletId ?: DefaultData.DEFAULT_WALLET_ID
                val toId = tx.toWalletId
                map[fromId] = (map[fromId] ?: 0L) - tx.amount
                if (toId != null) {
                    map[toId] = (map[toId] ?: 0L) + tx.amount
                }
            }
            "penyesuaian" -> {
                map[wId] = (map[wId] ?: 0L) + tx.amount
            }
        }
    }

    return map
}

/**
 * Menghitung ringkasan budget untuk Beranda sesuai web (lines 7108-7250 desktop/src/js/app.js).
 * Hanya berlaku untuk mata uang IDR. Jika tidak ada budget tersimpan, mengembalikan null.
 */
fun getBudgetSectionSummary(
    budgets: BudgetsData,
    categories: CategoriesData,
    transactions: List<TransactionEntity>,
    targetMonthKey: String,
    wallets: List<WalletEntity>
): BudgetSectionSummary? {
    val budgetMap = budgets.items.filter { it.value > 0 }
    if (budgetMap.isEmpty()) return null

    // 1. Hitung total pengeluaran per kategori IDR pada bulan terpilih
    val categoryTotals = HashMap<String, Long>()
    val subcategoryTotals = HashMap<String, Long>()

    for (tx in transactions) {
        if (tx.type != "expense" || tx.deleted || tx.amount <= 0L || tx.date.isBlank()) continue
        if (Uang.getTransactionCurrency(tx, wallets) != "IDR") continue
        val txMonth = tx.date.take(7)
        if (txMonth == targetMonthKey) {
            val cat = tx.category ?: "Lainnya"
            categoryTotals[cat] = (categoryTotals[cat] ?: 0L) + tx.amount
            val sub = tx.subcategory?.trim()
            if (!sub.isNullOrEmpty()) {
                val subKey = "$cat|||$sub"
                subcategoryTotals[subKey] = (subcategoryTotals[subKey] ?: 0L) + tx.amount
            }
        }
    }

    // 2. Susun urutan kategori (kategori pengeluaran aktif dulu, lalu orphan)
    val activeExpenseList = categories.expense.map { it.name }
    val orderedCats = mutableListOf<String>()
    for (cat in activeExpenseList) {
        if (budgetMap.containsKey(cat)) {
            orderedCats.add(cat)
        }
    }
    val orphanCats = budgetMap.keys.filter { !activeExpenseList.contains(it) }.sorted()
    orderedCats.addAll(orphanCats)

    var totalBudget = 0L
    var totalTerpakai = 0L
    val items = mutableListOf<BudgetItemSummary>()

    for (cat in orderedCats) {
        val budgetAmount = budgetMap[cat] ?: continue
        val terpakai = categoryTotals[cat] ?: 0L
        totalBudget += budgetAmount
        totalTerpakai += terpakai

        val percent = Uang.hitungPersen(terpakai, budgetAmount)
        val isOver = terpakai > budgetAmount
        val sisa = if (isOver) 0L else (budgetAmount - terpakai)
        val lebih = if (isOver) (terpakai - budgetAmount) else 0L

        // Sub kategori budget
        val definedSubs = categories.subcategories.expense[cat]?.map { it.name } ?: emptyList()
        val subBudgetMap = budgets.subcategories[cat]?.filter { it.value > 0 } ?: emptyMap()
        val orderedSubs = mutableListOf<String>()
        for (s in definedSubs) {
            if (subBudgetMap.containsKey(s)) orderedSubs.add(s)
        }
        for (s in subBudgetMap.keys) {
            if (!orderedSubs.contains(s)) orderedSubs.add(s)
        }

        val subItemList = mutableListOf<SubcategoryBudgetItemSummary>()
        for (sub in orderedSubs) {
            val subBudgetAmount = subBudgetMap[sub] ?: continue
            val subTerpakai = subcategoryTotals["$cat|||$sub"] ?: 0L
            val subPercent = Uang.hitungPersen(subTerpakai, subBudgetAmount)
            val subIsOver = subTerpakai > subBudgetAmount
            val subSisa = if (subIsOver) 0L else (subBudgetAmount - subTerpakai)
            val subLebih = if (subIsOver) (subTerpakai - subBudgetAmount) else 0L

            subItemList.add(
                SubcategoryBudgetItemSummary(
                    subcategoryName = sub,
                    budgetAmount = subBudgetAmount,
                    terpakaiAmount = subTerpakai,
                    percent = subPercent,
                    isOver = subIsOver,
                    sisaAmount = subSisa,
                    lebihAmount = subLebih
                )
            )
        }

        items.add(
            BudgetItemSummary(
                categoryName = cat,
                budgetAmount = budgetAmount,
                terpakaiAmount = terpakai,
                percent = percent,
                isOver = isOver,
                sisaAmount = sisa,
                lebihAmount = lebih,
                subcategories = subItemList
            )
        )
    }

    return BudgetSectionSummary(
        items = items,
        totalBudget = totalBudget,
        totalTerpakai = totalTerpakai
    )
}
