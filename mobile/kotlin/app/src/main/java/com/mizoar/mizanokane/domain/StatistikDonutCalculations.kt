package com.mizoar.mizanokane.domain

import androidx.compose.ui.graphics.Color
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import java.util.Locale
import kotlin.math.max

/**
 * Palet warna diagram donut sesuai token dev/DESIGN.md dan desktop/src/js/app.js.
 */
val DONUT_EXPENSE_DARK = listOf(
    Color(0xFFFF453A), // Red / Tone Error
    Color(0xFFF5A623), // Amber / Warn
    Color(0xFF4A9EFF), // Blue / Transfer
    Color(0xFF2ECC8F), // Teal-Green Accent Dark
    Color(0xFF9194A1), // Text Secondary Dark
    Color(0xFF2A2D3A), // Border Dark
    Color(0xFF1F2232)  // Surface Raised Dark
)

val DONUT_EXPENSE_LIGHT = listOf(
    Color(0xFFFF453A), // Red / Tone Error
    Color(0xFFF5A623), // Amber / Warn
    Color(0xFF4A9EFF), // Blue / Transfer
    Color(0xFF24A574), // Green Accent Light
    Color(0xFF6B6D78), // Text Secondary Light
    Color(0xFFD8D8DC), // Border Light
    Color(0xFFF0F0F2)  // Surface Raised Light
)

val DONUT_INCOME_DARK = listOf(
    Color(0xFF2ECC8F), // Teal-Green Accent Dark
    Color(0xFF4A9EFF), // Blue / Transfer
    Color(0xFFF5A623), // Amber / Warn
    Color(0xFFFF453A), // Red / Tone Error
    Color(0xFF9194A1), // Text Secondary Dark
    Color(0xFF2A2D3A), // Border Dark
    Color(0xFF1F2232)  // Surface Raised Dark
)

val DONUT_INCOME_LIGHT = listOf(
    Color(0xFF24A574), // Green Accent Light
    Color(0xFF4A9EFF), // Blue / Transfer
    Color(0xFFF5A623), // Amber / Warn
    Color(0xFFFF453A), // Red / Tone Error
    Color(0xFF6B6D78), // Text Secondary Light
    Color(0xFFD8D8DC), // Border Light
    Color(0xFFF0F0F2)  // Surface Raised Light
)

data class DonutSubcategoryItem(
    val subName: String,
    val amount: Long,
    val count: Int,
    val percentFormatted: String,
    val isNoSubcategory: Boolean = false
)

data class DonutCategoryDetail(
    val category: String,
    val amount: Long,
    val count: Int,
    val percentageFormatted: String,
    val subcategories: List<DonutSubcategoryItem>
)

data class DonutCategoryItem(
    val category: String,
    val amount: Long,
    val count: Int,
    val percentInt: Int,
    val percentageDouble: Double,
    val percentageFormatted: String,
    val color: Color,
    val startAngle: Float,
    val sweepAngle: Float,
    val detail: DonutCategoryDetail
)

data class DonutChartData(
    val items: List<DonutCategoryItem>,
    val totalAmount: Long,
    val totalCategoriesCount: Int,
    val totalTransactionsCount: Int
)

private data class CatStatsAccumulator(
    var amount: Long = 0L,
    var count: Int = 0
)

/**
 * Resolusi nama kategori untuk statistik.
 * Transaksi tipe penyesuaian dikelompokkan ke kategori "Equilibrium".
 * Kategori kosong difallback ke "Lainnya".
 * Sama persis dengan desktop/src/js/app.js lines 635-641.
 */
fun getTxStatsCategory(tx: TransactionEntity?): String {
    if (tx == null) return "Lainnya"
    if (tx.type == "penyesuaian") {
        return "Equilibrium"
    }
    val cat = tx.category?.trim().orEmpty()
    return if (cat.isNotEmpty()) cat else "Lainnya"
}

/**
 * Resolusi nama sub kategori transaksi.
 * Transaksi penyesuaian memakai subcategory jika ada, atau category jika != Equilibrium, atau "Koreksi".
 * Sama persis dengan desktop/src/js/app.js lines 583-593.
 */
fun getTxSubcategory(tx: TransactionEntity?): String {
    if (tx == null || tx.type == "transfer") return ""
    if (tx.type == "penyesuaian") {
        val sub = tx.subcategory?.trim().orEmpty()
        if (sub.isNotEmpty()) return sub
        val cat = tx.category?.trim().orEmpty()
        if (cat.isNotEmpty() && !cat.equals("Equilibrium", ignoreCase = true)) {
            return cat
        }
        return "Koreksi"
    }
    return tx.subcategory?.trim().orEmpty()
}

/**
 * Pemformat persentase untuk diagram donut.
 * Jika pct < 0.1 && pct > 0: "< 0.1%"
 * Jika desimal 0: tanpa koma desimal ("25%")
 * Jika ada desimal: satu desimal ("33.3%")
 * Sama persis dengan formatPercent di desktop/src/js/app.js lines 4043-4046.
 */
fun formatDonutPercent(pct: Double): String {
    if (pct < 0.1 && pct > 0.0) return "< 0.1%"
    return if (pct % 1.0 == 0.0) {
        "${pct.toLong()}%"
    } else {
        String.format(Locale.US, "%.1f%%", pct)
    }
}

/**
 * Menghitung data diagram donut per kategori untuk periode dan jenis transaksi yang dipilih.
 * Fungsi murni, persis mengikuti renderDonutChart di desktop/src/js/app.js.
 */
fun getDonutChartData(
    transactions: List<TransactionEntity>,
    wallets: List<WalletEntity>,
    selectedStatsMonth: String,
    currentScale: StatsScale,
    currentDonutType: StatsChartType,
    currentStatsCurrency: String,
    isDark: Boolean
): DonutChartData? {
    val selectedYStr = selectedStatsMonth.substringBefore('-')

    // 1. Filter transaksi
    val periodTx = transactions.filter { tx ->
        if (!isTxMatchingStatsType(tx, currentDonutType)) return@filter false
        if (tx.type == "transfer") return@filter false
        if (tx.deleted) return@filter false
        if (tx.date.isBlank()) return@filter false
        if (Uang.getTransactionCurrency(tx, wallets) != currentStatsCurrency) return@filter false

        val match = Regex("""^(\d{4})-(\d{1,2})""").find(tx.date) ?: return@filter false
        val year = match.groupValues[1]
        val monthInt = match.groupValues[2].toIntOrNull() ?: return@filter false
        val month = String.format(Locale.US, "%02d", monthInt)

        if (currentScale == StatsScale.YEARLY) {
            year == selectedYStr
        } else {
            "$year-$month" == selectedStatsMonth
        }
    }

    if (periodTx.isEmpty()) {
        return null
    }

    // 2. Kelompokkan per kategori
    val categoryMap = LinkedHashMap<String, CatStatsAccumulator>()
    var totalAmount = 0L
    for (tx in periodTx) {
        val cat = getTxStatsCategory(tx)
        val item = categoryMap.getOrPut(cat) { CatStatsAccumulator() }
        val amt = getTxEffectiveAmount(tx)
        item.amount += amt
        item.count += 1
        totalAmount += amt
    }

    if (totalAmount <= 0L) {
        return null
    }

    // 3. Urutkan kategori berdasarkan nominal terbesar descending
    val sortedCategories = categoryMap.entries
        .sortedWith { a, b ->
            val cmp = b.value.amount.compareTo(a.value.amount)
            if (cmp != 0) cmp else a.key.compareTo(b.key)
        }

    // 4. Pilih palet warna aktif
    val activePalette = if (currentDonutType == StatsChartType.INCOME) {
        if (isDark) DONUT_INCOME_DARK else DONUT_INCOME_LIGHT
    } else {
        if (isDark) DONUT_EXPENSE_DARK else DONUT_EXPENSE_LIGHT
    }

    val n = sortedCategories.size
    val circumference = 2.0 * Math.PI * 75.0 // r = 75 sesuai web
    val gapInDegrees = if (n > 1) ((2.0 / circumference) * 360.0).toFloat() else 0f
    var accumulatedAngle = -90f // Mulai dari arah jam 12 (-90 derajat)

    // 5. Susun item kategori beserta rincian sub kategori
    val items = sortedCategories.mapIndexed { index, (catName, catData) ->
        val pctDouble = if (totalAmount > 0L) (catData.amount.toDouble() / totalAmount.toDouble()) * 100.0 else 0.0
        val pctFormatted = formatDonutPercent(pctDouble)
        val percentInt = Uang.hitungPersen(catData.amount, totalAmount)
        val color = activePalette[index % activePalette.size]

        val fullSweep = if (totalAmount > 0L) (catData.amount.toFloat() / totalAmount.toFloat()) * 360f else 0f
        val visibleSweep = if (n > 1) max(0.5f, fullSweep - gapInDegrees) else fullSweep
        val startAngle = accumulatedAngle
        accumulatedAngle += fullSweep

        // Ambil transaksi khusus kategori ini untuk rincian sub kategori
        val catTx = periodTx.filter { getTxStatsCategory(it) == catName }
        val subMap = LinkedHashMap<String, CatStatsAccumulator>()
        var noSubAmount = 0L
        var noSubCount = 0
        for (tx in catTx) {
            val sub = getTxSubcategory(tx)
            val amt = getTxEffectiveAmount(tx)
            if (sub.isNotBlank()) {
                val sItem = subMap.getOrPut(sub) { CatStatsAccumulator() }
                sItem.amount += amt
                sItem.count += 1
            } else {
                noSubAmount += amt
                noSubCount += 1
            }
        }

        val sortedSubs = subMap.entries
            .sortedWith { a, b ->
                val cmp = b.value.amount.compareTo(a.value.amount)
                if (cmp != 0) cmp else a.key.compareTo(b.key)
            }

        val subItems = mutableListOf<DonutSubcategoryItem>()
        if (subMap.isNotEmpty()) {
            for ((subName, subData) in sortedSubs) {
                val subPctDouble = if (catData.amount > 0L) (subData.amount.toDouble() / catData.amount.toDouble()) * 100.0 else 0.0
                val subPctStr = formatDonutPercent(subPctDouble)
                subItems.add(
                    DonutSubcategoryItem(
                        subName = subName,
                        amount = subData.amount,
                        count = subData.count,
                        percentFormatted = subPctStr,
                        isNoSubcategory = false
                    )
                )
            }
            if (noSubAmount > 0L) {
                val noSubPctDouble = if (catData.amount > 0L) (noSubAmount.toDouble() / catData.amount.toDouble()) * 100.0 else 0.0
                val noSubPctStr = formatDonutPercent(noSubPctDouble)
                subItems.add(
                    DonutSubcategoryItem(
                        subName = "Tanpa sub kategori",
                        amount = noSubAmount,
                        count = noSubCount,
                        percentFormatted = noSubPctStr,
                        isNoSubcategory = true
                    )
                )
            }
        }

        val detail = DonutCategoryDetail(
            category = catName,
            amount = catData.amount,
            count = catData.count,
            percentageFormatted = pctFormatted,
            subcategories = subItems
        )

        DonutCategoryItem(
            category = catName,
            amount = catData.amount,
            count = catData.count,
            percentInt = percentInt,
            percentageDouble = pctDouble,
            percentageFormatted = pctFormatted,
            color = color,
            startAngle = startAngle,
            sweepAngle = visibleSweep,
            detail = detail
        )
    }

    return DonutChartData(
        items = items,
        totalAmount = totalAmount,
        totalCategoriesCount = items.size,
        totalTransactionsCount = periodTx.size
    )
}
