package com.mizoar.mizanokane.domain

import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity

data class TimbanganMonthlyData(
    val income: Long,
    val expense: Long,
    val diff: Long,
    val txCount: Int
)

/**
 * Menghitung data pemasukan, pengeluaran, selisih (surplus/defisit),
 * dan jumlah transaksi untuk bulan dan mata uang terpilih sesuai web
 * (lines 4418-4452 desktop/src/js/app.js).
 */
fun getTimbanganMonthlyData(
    transactions: List<TransactionEntity>,
    targetMonthKey: String,
    currency: String,
    wallets: List<WalletEntity>
): TimbanganMonthlyData {
    var income = 0L
    var expense = 0L
    var txCount = 0

    for (tx in transactions) {
        if (tx.deleted || tx.amount <= 0L || tx.date.isBlank()) continue
        if (tx.type == "transfer" || tx.type == "penyesuaian") continue
        if (Uang.getTransactionCurrency(tx, wallets) != currency) continue

        val txMonth = tx.date.take(7)
        if (txMonth == targetMonthKey) {
            when (tx.type) {
                "income" -> {
                    income += tx.amount
                    txCount++
                }
                "expense" -> {
                    expense += tx.amount
                    txCount++
                }
            }
        }
    }

    val diff = income - expense
    return TimbanganMonthlyData(
        income = income,
        expense = expense,
        diff = diff,
        txCount = txCount
    )
}

/**
 * Menghitung kemiringan lengan timbangan statis (dy) berdasarkan perbandingan selisih
 * terhadap total arus kas bulanan sesuai web (lines 4454-4467 desktop/src/js/app.js).
 */
fun calculateScaleTiltDy(
    income: Long,
    expense: Long,
    diff: Long,
    maxTilt: Int = 16
): Int {
    val total = income + expense
    if (total <= 0L) return 0
    val ratio = diff.toDouble() / total.toDouble()
    return when {
        diff > 0L -> kotlin.math.max(3, kotlin.math.min(maxTilt, kotlin.math.round(ratio * maxTilt).toInt()))
        diff < 0L -> kotlin.math.min(-3, kotlin.math.max(-maxTilt, kotlin.math.round(ratio * maxTilt).toInt()))
        else -> 0
    }
}

data class EquilibriumCurrencyTotal(
    val currency: String,
    val total: Long
)

/**
 * Menghitung total penyesuaian saldo (Equilibrium) pada bulan terpilih dikelompokkan
 * per mata uang sesuai web (lines 4750-4803 desktop/src/js/app.js).
 */
fun getTimbanganEquilibriumTotals(
    transactions: List<TransactionEntity>,
    targetMonthKey: String,
    wallets: List<WalletEntity>
): List<EquilibriumCurrencyTotal> {
    val totalsByCurrency = LinkedHashMap<String, Long>()

    for (tx in transactions) {
        if (tx.deleted || tx.type != "penyesuaian" || tx.date.isBlank()) continue
        val txMonth = tx.date.take(7)
        if (txMonth != targetMonthKey) continue

        val curr = Uang.getTransactionCurrency(tx, wallets)
        totalsByCurrency[curr] = (totalsByCurrency[curr] ?: 0L) + tx.amount
    }

    if (totalsByCurrency.isEmpty()) return emptyList()

    val currencyOrder = mapOf("IDR" to 0, "JPY" to 1, "USD" to 2)
    return totalsByCurrency.entries
        .sortedBy { currencyOrder[it.key] ?: 99 }
        .map { EquilibriumCurrencyTotal(it.key, it.value) }
}

enum class TimbangStatus {
    AMAN, PAS, BERAT
}

data class TimbangCalculatorResult(
    val status: TimbangStatus,
    val statusBadge: String,
    val statusDesc: String,
    val newWalletBal: Long,
    val hasBudget: Boolean,
    val remainingBudget: Long,
    val remainingPercent: Int,
    val newMonthDiff: Long
)

/**
 * Menghitung simulasi kalkulator "Timbang Sebelum Beli" sesuai web
 * (lines 4619-4748 desktop/src/js/app.js).
 */
fun calculateTimbangSebelumBeli(
    price: Long,
    selectedWalletId: String?,
    selectedCategory: String?,
    currency: String,
    targetMonthKey: String,
    wallets: List<WalletEntity>,
    walletBalances: Map<String, Long>,
    transactions: List<TransactionEntity>,
    budgets: BudgetsData
): TimbangCalculatorResult {
    val walletBal = if (selectedWalletId != null) walletBalances[selectedWalletId] ?: 0L else 0L
    val newWalletBal = walletBal - price

    val budgetMap = budgets.items.filter { it.value > 0L }
    val hasBudget = (currency == "IDR") && (!selectedCategory.isNullOrBlank()) && (budgetMap.containsKey(selectedCategory))
    val budgetAmount = if (hasBudget) budgetMap[selectedCategory] ?: 0L else 0L

    var remainingBudget = 0L
    var remainingPercent = 0

    if (hasBudget) {
        var currentSpent = 0L
        for (tx in transactions) {
            if (tx.type != "expense" || tx.deleted || tx.amount <= 0L || tx.date.isBlank()) continue
            if (Uang.getTransactionCurrency(tx, wallets) != "IDR") continue
            if (tx.date.take(7) == targetMonthKey && tx.category == selectedCategory) {
                currentSpent += tx.amount
            }
        }
        val newSpent = currentSpent + price
        remainingBudget = budgetAmount - newSpent
        remainingPercent = if (budgetAmount > 0L) {
            ((remainingBudget.toDouble() / budgetAmount.toDouble()) * 100).toInt()
        } else 0
    }

    val monthlyData = getTimbanganMonthlyData(transactions, targetMonthKey, currency, wallets)
    val monthIncome = monthlyData.income
    val currentMonthExpense = monthlyData.expense
    val newMonthExpense = currentMonthExpense + price
    val newMonthDiff = monthIncome - newMonthExpense

    val status: TimbangStatus
    val statusBadge: String
    val statusDesc: String

    if (hasBudget) {
        if (remainingBudget < 0L || newWalletBal < 0L) {
            status = TimbangStatus.BERAT
            statusBadge = "Berat"
            statusDesc = if (remainingBudget < 0L && newWalletBal < 0L) {
                "Lewat budget Rp ${Uang.formatNumber(kotlin.math.abs(remainingBudget) / 100)} dan saldo dompet jadi minus."
            } else if (remainingBudget < 0L) {
                "Melebihi budget bulanan Rp ${Uang.formatNumber(kotlin.math.abs(remainingBudget) / 100)}."
            } else {
                "Saldo dompet tidak mencukupi (jadi minus)."
            }
        } else if (remainingPercent <= 20) {
            status = TimbangStatus.PAS
            statusBadge = "Pas"
            statusDesc = "Sisa budget kategori tinggal $remainingPercent% (${Uang.formatCurrency(remainingBudget, "IDR")})."
        } else {
            status = TimbangStatus.AMAN
            statusBadge = "Aman"
            statusDesc = "Sisa budget masih aman di atas 20% ($remainingPercent% tersisa)."
        }
    } else {
        if (newWalletBal < 0L || newMonthDiff < 0L) {
            status = TimbangStatus.BERAT
            statusBadge = "Berat"
            statusDesc = if (newWalletBal < 0L && newMonthDiff < 0L) {
                "Saldo dompet jadi minus dan timbangan bulan ini defisit ${Uang.formatCurrency(kotlin.math.abs(newMonthDiff), currency)}."
            } else if (newWalletBal < 0L) {
                "Saldo dompet tidak mencukupi (jadi minus)."
            } else {
                "Timbangan bulan ini menjadi defisit ${Uang.formatCurrency(kotlin.math.abs(newMonthDiff), currency)}."
            }
        } else if (
            newMonthDiff == 0L ||
            newWalletBal == 0L ||
            (monthIncome > 0L && (newMonthDiff.toDouble() / monthIncome.toDouble()) <= 0.20) ||
            (walletBal > 0L && (newWalletBal.toDouble() / walletBal.toDouble()) <= 0.20)
        ) {
            status = TimbangStatus.PAS
            statusBadge = "Pas"
            statusDesc = "Saldo dompet atau surplus bulan ini pas-pasan (0–20%)."
        } else {
            status = TimbangStatus.AMAN
            statusBadge = "Aman"
            statusDesc = "Saldo dompet dan surplus timbangan bulan ini aman."
        }
    }

    return TimbangCalculatorResult(
        status = status,
        statusBadge = statusBadge,
        statusDesc = statusDesc,
        newWalletBal = newWalletBal,
        hasBudget = hasBudget,
        remainingBudget = remainingBudget,
        remainingPercent = remainingPercent,
        newMonthDiff = newMonthDiff
    )
}
