package com.mizoar.mizanokane.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.domain.DailyChartData
import com.mizoar.mizanokane.domain.MonthlyChartData
import com.mizoar.mizanokane.domain.StatsChartType
import com.mizoar.mizanokane.domain.StatsScale
import com.mizoar.mizanokane.domain.YearlyChartData
import com.mizoar.mizanokane.domain.formatMonthYear
import com.mizoar.mizanokane.domain.getActiveCurrencies
import com.mizoar.mizanokane.domain.getCurrentYearMonthString
import com.mizoar.mizanokane.domain.getDailyChartData
import com.mizoar.mizanokane.domain.getMonthlyChartData
import com.mizoar.mizanokane.domain.getYearlyChartData
import com.mizoar.mizanokane.domain.shiftMonth
import com.mizoar.mizanokane.domain.shiftYear
import com.mizoar.mizanokane.ui.theme.MizanTheme
import java.util.Calendar

private data class ChartBarItem(
    val label: String,
    val amount: Long,
    val isSelected: Boolean
)

@Composable
fun StatistikScreen(
    repository: MizanRepository,
    onNavigateToTransactionForm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transactions by repository.getAllTransactionsFlow().collectAsState(initial = emptyList())
    val wallets by repository.getAllWalletsFlow().collectAsState(initial = emptyList())

    val availableCurrencies = remember(wallets) { getActiveCurrencies(wallets) }
    var selectedCurrency by remember { mutableStateOf("IDR") }

    LaunchedEffect(availableCurrencies) {
        if (selectedCurrency !in availableCurrencies) {
            selectedCurrency = "IDR"
        }
    }

    var currentScale by remember { mutableStateOf(StatsScale.MONTHLY) }
    var currentChartType by remember { mutableStateOf(StatsChartType.EXPENSE) }
    var selectedStatsMonth by remember { mutableStateOf(getCurrentYearMonthString()) }

    var windowStart by remember { mutableStateOf<Int?>(null) }
    var windowEnd by remember { mutableStateOf<Int?>(null) }

    val monthlyData = remember(
        transactions,
        wallets,
        selectedStatsMonth,
        currentChartType,
        selectedCurrency,
        windowStart,
        windowEnd
    ) {
        getMonthlyChartData(
            transactions = transactions,
            wallets = wallets,
            selectedStatsMonth = selectedStatsMonth,
            currentChartType = currentChartType,
            currentStatsCurrency = selectedCurrency,
            currentWindowStart = windowStart,
            currentWindowEnd = windowEnd
        )
    }

    LaunchedEffect(monthlyData) {
        if (monthlyData != null) {
            windowStart = monthlyData.windowStartTotalMonths
            windowEnd = monthlyData.windowEndTotalMonths
        }
    }

    val dailyData = remember(
        transactions,
        wallets,
        selectedStatsMonth,
        currentChartType,
        selectedCurrency
    ) {
        getDailyChartData(
            transactions = transactions,
            wallets = wallets,
            selectedStatsMonth = selectedStatsMonth,
            currentChartType = currentChartType,
            currentStatsCurrency = selectedCurrency
        )
    }

    val yearlyData = remember(
        transactions,
        wallets,
        selectedStatsMonth,
        currentChartType,
        selectedCurrency
    ) {
        getYearlyChartData(
            transactions = transactions,
            wallets = wallets,
            selectedStatsMonth = selectedStatsMonth,
            currentChartType = currentChartType,
            currentStatsCurrency = selectedCurrency
        )
    }

    val currentMonth = getCurrentYearMonthString()
    val isYearly = currentScale == StatsScale.YEARLY
    val isNextEnabled = if (isYearly) {
        val currYear = currentMonth.split('-')[0].toIntOrNull() ?: 2026
        val selectedYear = selectedStatsMonth.split('-')[0].toIntOrNull() ?: 2026
        selectedYear < currYear
    } else {
        selectedStatsMonth < currentMonth
    }

    val periodLabel = if (isYearly) {
        selectedStatsMonth.split('-')[0]
    } else {
        formatMonthYear(selectedStatsMonth)
    }

    val chartSectionTitle = when (currentChartType) {
        StatsChartType.EXPENSE -> when (currentScale) {
            StatsScale.DAILY -> stringResource(id = R.string.title_grafik_pengeluaran_harian)
            StatsScale.MONTHLY -> stringResource(id = R.string.title_grafik_pengeluaran_bulanan)
            StatsScale.YEARLY -> stringResource(id = R.string.title_grafik_pengeluaran_tahunan)
        }
        StatsChartType.INCOME -> when (currentScale) {
            StatsScale.DAILY -> stringResource(id = R.string.title_grafik_pemasukan_harian)
            StatsScale.MONTHLY -> stringResource(id = R.string.title_grafik_pemasukan_bulanan)
            StatsScale.YEARLY -> stringResource(id = R.string.title_grafik_pemasukan_tahunan)
        }
    }

    val chartSummaryText = when (currentScale) {
        StatsScale.MONTHLY -> {
            monthlyData?.let {
                stringResource(
                    id = R.string.summary_avg_bulan,
                    Uang.formatCurrency(it.avgAmount, selectedCurrency)
                )
            } ?: ""
        }
        StatsScale.DAILY -> {
            dailyData?.let {
                stringResource(
                    id = R.string.summary_avg_hari,
                    Uang.formatCurrency(it.avgAmount, selectedCurrency)
                )
            } ?: ""
        }
        StatsScale.YEARLY -> {
            yearlyData?.let {
                stringResource(
                    id = R.string.summary_avg_tahun,
                    Uang.formatCurrency(it.avgAmount, selectedCurrency)
                )
            } ?: ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 1. Tab Skala Waktu (Harian, Bulanan, Tahunan)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MizanTheme.colors.surfaceRaised,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = MizanTheme.colors.border,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            ScaleTabButton(
                text = stringResource(id = R.string.tab_scale_harian),
                isSelected = currentScale == StatsScale.DAILY,
                onClick = { currentScale = StatsScale.DAILY },
                modifier = Modifier.weight(1f)
            )
            ScaleTabButton(
                text = stringResource(id = R.string.tab_scale_bulanan),
                isSelected = currentScale == StatsScale.MONTHLY,
                onClick = { currentScale = StatsScale.MONTHLY },
                modifier = Modifier.weight(1f)
            )
            ScaleTabButton(
                text = stringResource(id = R.string.tab_scale_tahunan),
                isSelected = currentScale == StatsScale.YEARLY,
                onClick = { currentScale = StatsScale.YEARLY },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Navigasi Periode (Bulan / Tahun)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MizanTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = MizanTheme.colors.border,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 14.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        selectedStatsMonth = if (isYearly) {
                            shiftYear(selectedStatsMonth, -1)
                        } else {
                            shiftMonth(selectedStatsMonth, -1, maxMonth = null)
                        }
                    },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_left),
                        contentDescription = if (isYearly) {
                            stringResource(id = R.string.nav_prev_year)
                        } else {
                            stringResource(id = R.string.nav_prev_month)
                        },
                        tint = MizanTheme.colors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = periodLabel,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = MizanTheme.colors.text
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                IconButton(
                    onClick = {
                        selectedStatsMonth = if (isYearly) {
                            shiftYear(selectedStatsMonth, 1)
                        } else {
                            shiftMonth(selectedStatsMonth, 1, maxMonth = currentMonth)
                        }
                    },
                    enabled = isNextEnabled,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_right),
                        contentDescription = if (isYearly) {
                            stringResource(id = R.string.nav_next_year)
                        } else {
                            stringResource(id = R.string.nav_next_month)
                        },
                        tint = MizanTheme.colors.textSecondary,
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(if (isNextEnabled) 1.0f else 0.3f)
                    )
                }
            }
        }

        // 3. Pilihan Mata Uang (jika ada lebih dari 1 mata uang aktif)
        if (availableCurrencies.size > 1) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MizanTheme.colors.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MizanTheme.colors.border,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.label_mata_uang),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.textSecondary,
                        fontSize = 13.sp
                    )
                )
                Row(
                    modifier = Modifier
                        .background(
                            color = MizanTheme.colors.surfaceRaised,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MizanTheme.colors.border,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    CurrencyTabButton(
                        text = stringResource(id = R.string.currency_rupiah),
                        isSelected = selectedCurrency == "IDR",
                        onClick = { selectedCurrency = "IDR" }
                    )
                    if ("JPY" in availableCurrencies) {
                        CurrencyTabButton(
                            text = stringResource(id = R.string.currency_yen),
                            isSelected = selectedCurrency == "JPY",
                            onClick = { selectedCurrency = "JPY" }
                        )
                    }
                    if ("USD" in availableCurrencies) {
                        CurrencyTabButton(
                            text = stringResource(id = R.string.currency_dolar),
                            isSelected = selectedCurrency == "USD",
                            onClick = { selectedCurrency = "USD" }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Kartu Bagian Grafik
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MizanTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = MizanTheme.colors.border,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Grafik (Judul, Rata-rata, Toggle Tipe Pengeluaran/Pemasukan)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = chartSectionTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MizanTheme.colors.text,
                                fontSize = 16.sp
                            )
                        )
                        if (chartSummaryText.isNotBlank()) {
                            Text(
                                text = chartSummaryText,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MizanTheme.colors.textSecondary,
                                    fontSize = 13.sp
                                ),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .background(
                                color = MizanTheme.colors.surfaceRaised,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MizanTheme.colors.border,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        TypeToggleButton(
                            text = stringResource(id = R.string.label_pengeluaran),
                            isSelected = currentChartType == StatsChartType.EXPENSE,
                            activeColor = MizanTheme.colors.danger,
                            onClick = { currentChartType = StatsChartType.EXPENSE }
                        )
                        TypeToggleButton(
                            text = stringResource(id = R.string.label_pemasukan),
                            isSelected = currentChartType == StatsChartType.INCOME,
                            activeColor = MizanTheme.colors.accent,
                            onClick = { currentChartType = StatsChartType.INCOME }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Detail Bar & Canvas Grafik
                when (currentScale) {
                    StatsScale.MONTHLY -> {
                        if (monthlyData == null) {
                            EmptyChartState(
                                chartType = currentChartType,
                                onNavigateToTransactionForm = onNavigateToTransactionForm
                            )
                        } else {
                            ChartDetailBar(
                                periodLabel = "${monthlyData.selectedItem.fullLabel}:",
                                totalAmount = monthlyData.selectedItem.amount,
                                count = monthlyData.selectedItem.count,
                                chartType = currentChartType,
                                currency = selectedCurrency
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            StatistikChartCanvas(
                                scale = currentScale,
                                chartType = currentChartType,
                                currency = selectedCurrency,
                                ceiling = monthlyData.ceiling,
                                midCeiling = monthlyData.midCeiling,
                                items = monthlyData.items.map {
                                    ChartBarItem(
                                        label = it.shortLabel,
                                        amount = it.amount,
                                        isSelected = it.isSelected
                                    )
                                },
                                selectedStatsMonth = selectedStatsMonth
                            )
                        }
                    }
                    StatsScale.DAILY -> {
                        if (dailyData == null) {
                            EmptyChartState(
                                chartType = currentChartType,
                                onNavigateToTransactionForm = onNavigateToTransactionForm
                            )
                        } else {
                            ChartDetailBar(
                                periodLabel = "${dailyData.fullMonthLabel}:",
                                totalAmount = dailyData.totalAmountMonth,
                                count = dailyData.totalTransactionsCount,
                                chartType = currentChartType,
                                currency = selectedCurrency
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            StatistikChartCanvas(
                                scale = currentScale,
                                chartType = currentChartType,
                                currency = selectedCurrency,
                                ceiling = dailyData.ceiling,
                                midCeiling = dailyData.midCeiling,
                                items = dailyData.items.map {
                                    ChartBarItem(
                                        label = it.label,
                                        amount = it.amount,
                                        isSelected = true
                                    )
                                },
                                selectedStatsMonth = selectedStatsMonth
                            )
                        }
                    }
                    StatsScale.YEARLY -> {
                        if (yearlyData == null) {
                            EmptyChartState(
                                chartType = currentChartType,
                                onNavigateToTransactionForm = onNavigateToTransactionForm
                            )
                        } else {
                            ChartDetailBar(
                                periodLabel = "${yearlyData.selectedItem.label}:",
                                totalAmount = yearlyData.selectedItem.amount,
                                count = yearlyData.selectedItem.count,
                                chartType = currentChartType,
                                currency = selectedCurrency
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            StatistikChartCanvas(
                                scale = currentScale,
                                chartType = currentChartType,
                                currency = selectedCurrency,
                                ceiling = yearlyData.ceiling,
                                midCeiling = yearlyData.midCeiling,
                                items = yearlyData.items.map {
                                    ChartBarItem(
                                        label = it.label,
                                        amount = it.amount,
                                        isSelected = it.isSelected
                                    )
                                },
                                selectedStatsMonth = selectedStatsMonth
                            )
                        }
                    }
                }
            }
        }

        // Bagian diagram donut per kategori akan ditambahkan di Kotlin 6B
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ScaleTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) MizanTheme.colors.surface else Color.Transparent)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = MizanTheme.colors.border,
                        shape = RoundedCornerShape(6.dp)
                    )
                } else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 13.5.sp,
                color = if (isSelected) MizanTheme.colors.text else MizanTheme.colors.textSecondary
            )
        )
    }
}

@Composable
private fun CurrencyTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = 36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) MizanTheme.colors.surface else Color.Transparent)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = MizanTheme.colors.border,
                        shape = RoundedCornerShape(4.dp)
                    )
                } else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isSelected) MizanTheme.colors.text else MizanTheme.colors.textSecondary
            )
        )
    }
}

@Composable
private fun TypeToggleButton(
    text: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = 36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) MizanTheme.colors.surface else Color.Transparent)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = MizanTheme.colors.border,
                        shape = RoundedCornerShape(4.dp)
                    )
                } else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isSelected) activeColor else MizanTheme.colors.textSecondary
            )
        )
    }
}

@Composable
private fun ChartDetailBar(
    periodLabel: String,
    totalAmount: Long,
    count: Int,
    chartType: StatsChartType,
    currency: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MizanTheme.colors.surfaceRaised,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = MizanTheme.colors.border,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = periodLabel,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MizanTheme.colors.text,
                fontSize = 13.5.sp
            )
        )
        Text(
            text = Uang.formatCurrency(totalAmount, currency),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (chartType == StatsChartType.INCOME) MizanTheme.colors.accent else MizanTheme.colors.danger,
                fontSize = 13.5.sp
            )
        )
        Text(
            text = if (count > 0) {
                stringResource(id = R.string.detail_count_transaksi, count)
            } else {
                if (chartType == StatsChartType.INCOME) {
                    stringResource(id = R.string.detail_empty_income)
                } else {
                    stringResource(id = R.string.detail_empty_expense)
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MizanTheme.colors.textSecondary,
                fontSize = 13.5.sp
            )
        )
    }
}

@Composable
private fun EmptyChartState(
    chartType: StatsChartType,
    onNavigateToTransactionForm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (chartType == StatsChartType.INCOME) {
                stringResource(id = R.string.empty_chart_income)
            } else {
                stringResource(id = R.string.empty_chart_expense)
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MizanTheme.colors.textSecondary,
                fontSize = 14.sp
            )
        )
        OutlinedButton(
            onClick = onNavigateToTransactionForm,
            modifier = Modifier.defaultMinSize(minHeight = 44.dp),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, MizanTheme.colors.accent),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MizanTheme.colors.accent
            )
        ) {
            Text(
                text = stringResource(id = R.string.action_tambah_transaksi),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
private fun StatistikChartCanvas(
    scale: StatsScale,
    chartType: StatsChartType,
    currency: String,
    ceiling: Long,
    midCeiling: Long,
    items: List<ChartBarItem>,
    selectedStatsMonth: String,
    modifier: Modifier = Modifier
) {
    val isScrollable = scale == StatsScale.DAILY || (scale == StatsScale.YEARLY && items.size > 6)
    val slotWidthDp = when {
        scale == StatsScale.DAILY -> 32.dp
        scale == StatsScale.YEARLY && isScrollable -> 64.dp
        else -> 0.dp
    }
    val chartWidthDp = if (isScrollable) {
        56.dp + 16.dp + (slotWidthDp * items.size)
    } else {
        0.dp
    }

    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    LaunchedEffect(scale, selectedStatsMonth, chartType, currency) {
        if (scale == StatsScale.DAILY) {
            if (selectedStatsMonth == getCurrentYearMonthString()) {
                val cal = Calendar.getInstance()
                val today = cal.get(Calendar.DAY_OF_MONTH)
                val paddingLeftPx = with(density) { 56.dp.toPx() }
                val slotPx = with(density) { 32.dp.toPx() }
                val halfContainerPx = with(density) { 160.dp.toPx() }
                val targetScroll = maxOf(0f, paddingLeftPx + (today - 1) * slotPx - halfContainerPx).toInt()
                scrollState.scrollTo(targetScroll)
            } else {
                scrollState.scrollTo(0)
            }
        } else if (scale == StatsScale.YEARLY && isScrollable) {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }

    val baseBarColor = if (chartType == StatsChartType.INCOME) {
        MizanTheme.colors.accent
    } else {
        MizanTheme.colors.danger
    }
    val borderColor = MizanTheme.colors.border
    val textColor = MizanTheme.colors.text
    val textSecondaryColor = MizanTheme.colors.textSecondary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isScrollable) {
                    Modifier.horizontalScroll(scrollState)
                } else Modifier
            )
    ) {
        Canvas(
            modifier = Modifier
                .then(
                    if (isScrollable) {
                        Modifier.width(chartWidthDp)
                    } else {
                        Modifier.fillMaxWidth()
                    }
                )
                .height(220.dp)
        ) {
            val paddingLeft = 56.dp.toPx()
            val paddingRight = 16.dp.toPx()
            val paddingTop = 26.dp.toPx()
            val paddingBottom = 34.dp.toPx()
            val plotHeight = size.height - paddingTop - paddingBottom
            val baselineY = paddingTop + plotHeight
            val totalWidth = size.width

            val n = items.size
            val slotWidth = if (isScrollable) {
                slotWidthDp.toPx()
            } else {
                (totalWidth - paddingLeft - paddingRight) / maxOf(1, n)
            }

            val barWidth = when (scale) {
                StatsScale.DAILY -> 16.dp.toPx()
                StatsScale.YEARLY -> minOf(36.dp.toPx(), maxOf(16.dp.toPx(), slotWidth * 0.52f))
                StatsScale.MONTHLY -> minOf(36.dp.toPx(), maxOf(14.dp.toPx(), slotWidth * 0.52f))
            }

            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()), 0f)
            val gridColor = borderColor.copy(alpha = 0.5f)

            // Garis Grid Atas (Ceiling)
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, paddingTop),
                end = Offset(totalWidth - paddingRight, paddingTop),
                strokeWidth = 1.dp.toPx(),
                pathEffect = dashEffect
            )

            // Garis Grid Tengah (MidCeiling)
            val midY = paddingTop + plotHeight / 2f
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, midY),
                end = Offset(totalWidth - paddingRight, midY),
                strokeWidth = 1.dp.toPx(),
                pathEffect = dashEffect
            )

            // Garis Sumbu Bawah (Baseline)
            drawLine(
                color = borderColor,
                start = Offset(paddingLeft, baselineY),
                end = Offset(totalWidth - paddingRight, baselineY),
                strokeWidth = 1.5.dp.toPx()
            )

            // Label Sumbu Y
            drawIntoCanvas { canvas ->
                val axisPaint = android.graphics.Paint().apply {
                    isAntiAlias = true
                    color = textSecondaryColor.toArgb()
                    textSize = 11.sp.toPx()
                    textAlign = android.graphics.Paint.Align.RIGHT
                }
                val labelX = paddingLeft - 8.dp.toPx()
                canvas.nativeCanvas.drawText(
                    Uang.formatCompactCurrency(ceiling, currency),
                    labelX,
                    paddingTop + 4.dp.toPx(),
                    axisPaint
                )
                canvas.nativeCanvas.drawText(
                    Uang.formatCompactCurrency(midCeiling, currency),
                    labelX,
                    midY + 4.dp.toPx(),
                    axisPaint
                )
                canvas.nativeCanvas.drawText(
                    "0",
                    labelX,
                    baselineY + 4.dp.toPx(),
                    axisPaint
                )
            }

            // Batang Grafik dan Label X
            items.forEachIndexed { index, item ->
                val cx = paddingLeft + (index + 0.5f) * slotWidth
                val barX = cx - barWidth / 2f
                val amount = item.amount
                val rawBarHeight = if (ceiling > 0L) (amount.toFloat() / ceiling.toFloat()) * plotHeight else 0f
                val barHeight = if (amount > 0L) maxOf(3.dp.toPx(), rawBarHeight) else 0f
                val barY = baselineY - barHeight

                val alpha = if (scale == StatsScale.DAILY || item.isSelected) 1.0f else 0.35f
                if (amount > 0L) {
                    drawRoundRect(
                        color = baseBarColor.copy(alpha = alpha),
                        topLeft = Offset(barX, barY),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                    )
                }

                drawIntoCanvas { canvas ->
                    // Label nilai di atas batang (jika ada nilai)
                    if (amount > 0L) {
                        val topLabel = Uang.formatCompactCurrency(amount, currency)
                        val labelY = maxOf(16.dp.toPx(), barY - 6.dp.toPx())
                        val valuePaint = android.graphics.Paint().apply {
                            isAntiAlias = true
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = 10.sp.toPx()
                            color = if (scale == StatsScale.DAILY || item.isSelected) textColor.toArgb() else textSecondaryColor.toArgb()
                            if (scale == StatsScale.DAILY || item.isSelected) {
                                typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                            }
                        }
                        canvas.nativeCanvas.drawText(topLabel, cx, labelY, valuePaint)
                    }

                    // Label nama periode di bawah baseline
                    val bottomLabelPaint = android.graphics.Paint().apply {
                        isAntiAlias = true
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = if (scale == StatsScale.DAILY) 11.sp.toPx() else 12.sp.toPx()
                        color = if (item.isSelected && scale != StatsScale.DAILY) textColor.toArgb() else textSecondaryColor.toArgb()
                        if (item.isSelected && scale != StatsScale.DAILY) {
                            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                        }
                    }
                    canvas.nativeCanvas.drawText(item.label, cx, baselineY + 18.dp.toPx(), bottomLabelPaint)
                }
            }
        }
    }
}
