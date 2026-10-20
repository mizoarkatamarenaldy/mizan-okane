package com.mizoar.mizanokane.domain

import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import java.util.Calendar
import java.util.Locale

enum class StatsScale {
    DAILY,
    MONTHLY,
    YEARLY
}

enum class StatsChartType {
    EXPENSE,
    INCOME
}

data class MonthlyChartItem(
    val yearMonth: String,
    val shortLabel: String,
    val fullLabel: String,
    val amount: Long,
    val count: Int,
    val isSelected: Boolean
)

data class MonthlyChartData(
    val items: List<MonthlyChartItem>,
    val totalAmountAll: Long,
    val avgAmount: Long,
    val selectedItem: MonthlyChartItem,
    val ceiling: Long,
    val midCeiling: Long,
    val windowStartTotalMonths: Int,
    val windowEndTotalMonths: Int
)

data class DailyChartItem(
    val day: Int,
    val label: String,
    val amount: Long,
    val count: Int
)

data class DailyChartData(
    val fullMonthLabel: String,
    val items: List<DailyChartItem>,
    val totalAmountMonth: Long,
    val avgAmount: Long,
    val totalTransactionsCount: Int,
    val ceiling: Long,
    val midCeiling: Long
)

data class YearlyChartItem(
    val year: Int,
    val label: String,
    val amount: Long,
    val count: Int,
    val isSelected: Boolean
)

data class YearlyChartData(
    val items: List<YearlyChartItem>,
    val totalAmountAll: Long,
    val avgAmount: Long,
    val selectedItem: YearlyChartItem,
    val ceiling: Long,
    val midCeiling: Long
)

/**
 * Filter apakah suatu transaksi relevan untuk grafik statistik sesuai jenis grafik
 * (pengeluaran atau pemasukan) dan status transaksi aktif (bukan soft-deleted dan memiliki tanggal).
 * Aturan sama dengan web (lines 612-625 desktop/src/js/app.js).
 */
fun isTxMatchingStatsType(tx: TransactionEntity?, targetType: StatsChartType): Boolean {
    if (tx == null || tx.deleted || tx.date.isBlank()) return false
    return when (targetType) {
        StatsChartType.INCOME -> {
            (tx.type == "income" && tx.amount > 0L) ||
            (tx.type == "penyesuaian" && tx.amount > 0L)
        }
        StatsChartType.EXPENSE -> {
            (tx.type == "expense" && tx.amount > 0L) ||
            (tx.type == "penyesuaian" && tx.amount < 0L)
        }
    }
}

/**
 * Mengambil nominal efektif transaksi. Penyesuaian equilibrium memakai nilai absolut.
 * Aturan sama dengan web (lines 627-633 desktop/src/js/app.js).
 */
fun getTxEffectiveAmount(tx: TransactionEntity?): Long {
    if (tx == null) return 0L
    return if (tx.type == "penyesuaian") {
        kotlin.math.abs(tx.amount)
    } else {
        tx.amount
    }
}

/**
 * Menghitung batas atas (ceiling) sumbu Y yang rapi berbasis integer Long seperseratus.
 * Aturan sama dengan web (lines 2949-2966 desktop/src/js/app.js).
 */
fun getNiceCeiling(maxCents: Long, currency: String): Long {
    if (maxCents <= 0L) {
        return when (currency) {
            "USD" -> 100_00L
            "JPY" -> 10_000_00L
            else -> 100_000_00L
        }
    }
    val targetCents = (maxCents * 115L + 50L) / 100L
    var factor = 1L
    while (factor <= targetCents / 10L) {
        factor *= 10L
    }
    val fractionTimes10 = (targetCents * 10L) / factor
    return when {
        fractionTimes10 <= 10L -> factor * 1L
        fractionTimes10 <= 20L -> factor * 2L
        fractionTimes10 <= 25L -> (factor * 5L) / 2L
        fractionTimes10 <= 50L -> factor * 5L
        else -> factor * 10L
    }
}

/**
 * Menggeser tahun sebanyak [delta] pada string "YYYY-MM" tanpa mengubah bulan jika memungkinkan,
 * dan dibatasi tidak boleh melewati tahun berjalan jika [delta] > 0.
 * Aturan sama dengan web (lines 3866-3878 desktop/src/js/app.js).
 */
fun shiftYear(yearMonth: String, delta: Int, maxYearMonth: String = getCurrentYearMonthString()): String {
    val parts = yearMonth.split('-')
    if (parts.size < 2) return yearMonth
    var y = parts[0].toIntOrNull() ?: return yearMonth
    var m = parts.getOrNull(1)?.toIntOrNull() ?: return yearMonth

    val maxParts = maxYearMonth.split('-')
    val currY = maxParts[0].toIntOrNull() ?: y
    val currM = maxParts.getOrNull(1)?.toIntOrNull() ?: m

    if (delta > 0 && y >= currY) {
        return yearMonth
    }
    y += delta
    if (delta > 0 && y > currY) {
        y = currY
    }
    if (y == currY && m > currM) {
        m = currM
    }
    return String.format(Locale.ROOT, "%04d-%02d", y, m)
}

private class MonthAccumulator {
    var total: Long = 0L
    var count: Int = 0
}

/**
 * Menghitung data grafik bulanan Statistik dengan aturan jendela geser 3-6 bulan
 * sesuai web (lines 3060-3224 desktop/src/js/app.js).
 */
fun getMonthlyChartData(
    transactions: List<TransactionEntity>,
    wallets: List<WalletEntity>,
    selectedStatsMonth: String,
    currentChartType: StatsChartType,
    currentStatsCurrency: String,
    currentWindowStart: Int? = null,
    currentWindowEnd: Int? = null
): MonthlyChartData? {
    val chartTx = transactions.filter { tx ->
        isTxMatchingStatsType(tx, currentChartType) &&
        tx.type != "transfer" &&
        !tx.deleted &&
        tx.date.isNotBlank() &&
        Uang.getTransactionCurrency(tx, wallets) == currentStatsCurrency
    }

    if (chartTx.isEmpty()) return null

    val monthData = LinkedHashMap<String, MonthAccumulator>()
    for (tx in chartTx) {
        val date = tx.date
        if (date.length < 7) continue
        val key = date.substring(0, 7)
        val acc = monthData.getOrPut(key) { MonthAccumulator() }
        acc.total += getTxEffectiveAmount(tx)
        acc.count += 1
    }

    if (monthData.isEmpty()) return null

    val activeMonth = selectedStatsMonth
    val activeParts = activeMonth.split('-')
    val activeY = activeParts[0].toIntOrNull() ?: 2026
    val activeM = activeParts.getOrNull(1)?.toIntOrNull() ?: 1
    val activeTotalMonths = activeY * 12 + (activeM - 1)

    val currMonthStr = getCurrentYearMonthString()
    val currParts = currMonthStr.split('-')
    val currY = currParts[0].toIntOrNull() ?: 2026
    val currM = currParts.getOrNull(1)?.toIntOrNull() ?: 1
    val currTotalMonths = currY * 12 + (currM - 1)

    val rawKeys = monthData.keys.sorted()
    val minKey = if (rawKeys.isNotEmpty()) rawKeys[0] else currMonthStr
    val minParts = minKey.split('-')
    val minY = minParts[0].toIntOrNull() ?: currY
    val minM = minParts.getOrNull(1)?.toIntOrNull() ?: currM
    val minTotalMonths = minY * 12 + (minM - 1)

    val monthsSpan = maxOf(1, currTotalMonths - minTotalMonths + 1)
    val windowLength = minOf(6, maxOf(3, monthsSpan))

    var startTotalMonths: Int
    var endTotalMonths: Int

    if (currentWindowStart == null || currentWindowEnd == null) {
        endTotalMonths = currTotalMonths
        startTotalMonths = endTotalMonths - (windowLength - 1)
    } else {
        startTotalMonths = currentWindowStart
        endTotalMonths = currentWindowEnd
    }

    if (activeTotalMonths < startTotalMonths) {
        startTotalMonths = activeTotalMonths
        endTotalMonths = startTotalMonths + (windowLength - 1)
    } else if (activeTotalMonths > endTotalMonths) {
        endTotalMonths = activeTotalMonths
        startTotalMonths = endTotalMonths - (windowLength - 1)
    }

    val displayMonths = mutableListOf<String>()
    for (mIdx in startTotalMonths..endTotalMonths) {
        val y = mIdx / 12
        val m = (mIdx % 12) + 1
        displayMonths.add(String.format(Locale.ROOT, "%04d-%02d", y, m))
    }

    var totalAmountAll = 0L
    var maxMonthAmount = 0L
    var monthsWithAmount = 0
    val items = mutableListOf<MonthlyChartItem>()

    for (key in displayMonths) {
        val acc = monthData[key]
        val amt = acc?.total ?: 0L
        val count = acc?.count ?: 0
        totalAmountAll += amt
        if (amt > maxMonthAmount) maxMonthAmount = amt
        if (amt > 0L) monthsWithAmount++

        val parts = key.split('-')
        val yStr = parts[0]
        val mNum = parts.getOrNull(1)?.toIntOrNull() ?: 1
        val shortName = INDO_MONTHS_SHORT.getOrNull(mNum - 1) ?: ""
        val shortYear = if (yStr.length >= 4) yStr.substring(2) else yStr
        val shortLabel = "$shortName '$shortYear"
        val fullLabel = formatMonthYear(key)
        val isSelected = (key == activeMonth)

        items.add(
            MonthlyChartItem(
                yearMonth = key,
                shortLabel = shortLabel,
                fullLabel = fullLabel,
                amount = amt,
                count = count,
                isSelected = isSelected
            )
        )
    }

    val avgAmount = if (monthsWithAmount > 0) totalAmountAll / monthsWithAmount else 0L
    val ceiling = getNiceCeiling(maxMonthAmount, currentStatsCurrency)
    val midCeiling = ceiling / 2L
    val selectedItem = items.find { it.isSelected } ?: MonthlyChartItem(
        yearMonth = activeMonth,
        shortLabel = "",
        fullLabel = formatMonthYear(activeMonth),
        amount = 0L,
        count = 0,
        isSelected = true
    )

    return MonthlyChartData(
        items = items,
        totalAmountAll = totalAmountAll,
        avgAmount = avgAmount,
        selectedItem = selectedItem,
        ceiling = ceiling,
        midCeiling = midCeiling,
        windowStartTotalMonths = startTotalMonths,
        windowEndTotalMonths = endTotalMonths
    )
}

private class DayAccumulator {
    var total: Long = 0L
    var count: Int = 0
}

/**
 * Menghitung data grafik harian Statistik untuk bulan terpilih (tanggal 1 sampai tanggal terakhir)
 * sesuai web (lines 3325-3433 desktop/src/js/app.js).
 */
fun getDailyChartData(
    transactions: List<TransactionEntity>,
    wallets: List<WalletEntity>,
    selectedStatsMonth: String,
    currentChartType: StatsChartType,
    currentStatsCurrency: String
): DailyChartData? {
    val chartTx = transactions.filter { tx ->
        isTxMatchingStatsType(tx, currentChartType) &&
        tx.type != "transfer" &&
        !tx.deleted &&
        tx.date.isNotBlank() &&
        Uang.getTransactionCurrency(tx, wallets) == currentStatsCurrency
    }

    if (chartTx.isEmpty()) return null

    val activeParts = selectedStatsMonth.split('-')
    val activeY = activeParts[0].toIntOrNull() ?: 2026
    val activeM = activeParts.getOrNull(1)?.toIntOrNull() ?: 1
    val fullMonthLabel = formatMonthYear(selectedStatsMonth)

    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, activeY)
        set(Calendar.MONTH, activeM - 1)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val dayData = HashMap<Int, DayAccumulator>()
    for (d in 1..daysInMonth) {
        dayData[d] = DayAccumulator()
    }

    val targetMonthTx = chartTx.filter { tx ->
        tx.date.startsWith(selectedStatsMonth)
    }

    for (tx in targetMonthTx) {
        val parts = tx.date.split('-')
        if (parts.size >= 3) {
            val d = parts[2].toIntOrNull() ?: continue
            val item = dayData[d]
            if (item != null) {
                item.total += getTxEffectiveAmount(tx)
                item.count += 1
            }
        }
    }

    var totalAmountMonth = 0L
    var maxDayAmount = 0L
    var daysWithAmount = 0
    val items = mutableListOf<DailyChartItem>()

    for (d in 1..daysInMonth) {
        val acc = dayData[d]
        val total = acc?.total ?: 0L
        val count = acc?.count ?: 0
        totalAmountMonth += total
        if (total > maxDayAmount) maxDayAmount = total
        if (total > 0L) daysWithAmount++

        items.add(
            DailyChartItem(
                day = d,
                label = d.toString(),
                amount = total,
                count = count
            )
        )
    }

    val avgAmount = if (daysWithAmount > 0) totalAmountMonth / daysWithAmount else 0L
    val ceiling = getNiceCeiling(maxDayAmount, currentStatsCurrency)
    val midCeiling = ceiling / 2L

    return DailyChartData(
        fullMonthLabel = fullMonthLabel,
        items = items,
        totalAmountMonth = totalAmountMonth,
        avgAmount = avgAmount,
        totalTransactionsCount = targetMonthTx.size,
        ceiling = ceiling,
        midCeiling = midCeiling
    )
}

private class YearAccumulator {
    var total: Long = 0L
    var count: Int = 0
}

/**
 * Menghitung data grafik tahunan Statistik dari tahun terawal sampai tahun berjalan
 * sesuai web (lines 3516-3632 desktop/src/js/app.js).
 */
fun getYearlyChartData(
    transactions: List<TransactionEntity>,
    wallets: List<WalletEntity>,
    selectedStatsMonth: String,
    currentChartType: StatsChartType,
    currentStatsCurrency: String
): YearlyChartData? {
    val chartTx = transactions.filter { tx ->
        isTxMatchingStatsType(tx, currentChartType) &&
        tx.type != "transfer" &&
        !tx.deleted &&
        tx.date.isNotBlank() &&
        Uang.getTransactionCurrency(tx, wallets) == currentStatsCurrency
    }

    if (chartTx.isEmpty()) return null

    val selectedY = selectedStatsMonth.split('-')[0].toIntOrNull() ?: 2026
    val currYear = getCurrentYearMonthString().split('-')[0].toIntOrNull() ?: 2026

    var earliestYear = currYear
    for (tx in chartTx) {
        val y = tx.date.split('-').getOrNull(0)?.toIntOrNull()
        if (y != null && y < earliestYear) {
            earliestYear = y
        }
    }

    val startYear = minOf(earliestYear, selectedY)
    val endYear = currYear

    val yearData = LinkedHashMap<Int, YearAccumulator>()
    for (y in startYear..endYear) {
        yearData[y] = YearAccumulator()
    }

    for (tx in chartTx) {
        val y = tx.date.split('-').getOrNull(0)?.toIntOrNull() ?: continue
        val item = yearData[y]
        if (item != null) {
            item.total += getTxEffectiveAmount(tx)
            item.count += 1
        }
    }

    var totalAmountAll = 0L
    var maxYearAmount = 0L
    var yearsWithAmount = 0
    val items = mutableListOf<YearlyChartItem>()

    for (y in startYear..endYear) {
        val acc = yearData[y]
        val total = acc?.total ?: 0L
        val count = acc?.count ?: 0
        totalAmountAll += total
        if (total > maxYearAmount) maxYearAmount = total
        if (total > 0L) yearsWithAmount++

        items.add(
            YearlyChartItem(
                year = y,
                label = y.toString(),
                amount = total,
                count = count,
                isSelected = (y == selectedY)
            )
        )
    }

    val avgAmount = if (yearsWithAmount > 0) totalAmountAll / yearsWithAmount else 0L
    val ceiling = getNiceCeiling(maxYearAmount, currentStatsCurrency)
    val midCeiling = ceiling / 2L
    val selectedItem = items.find { it.isSelected } ?: YearlyChartItem(
        year = selectedY,
        label = selectedY.toString(),
        amount = 0L,
        count = 0,
        isSelected = true
    )

    return YearlyChartData(
        items = items,
        totalAmountAll = totalAmountAll,
        avgAmount = avgAmount,
        selectedItem = selectedItem,
        ceiling = ceiling,
        midCeiling = midCeiling
    )
}
