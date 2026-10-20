package com.mizoar.mizanokane.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.CategoriesData
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.domain.DailyChartData
import com.mizoar.mizanokane.domain.DonutCategoryItem
import com.mizoar.mizanokane.domain.DonutChartData
import com.mizoar.mizanokane.domain.MonthlyChartData
import com.mizoar.mizanokane.domain.StatsChartType
import com.mizoar.mizanokane.domain.StatsScale
import com.mizoar.mizanokane.domain.YearlyChartData
import com.mizoar.mizanokane.domain.formatMonthYear
import com.mizoar.mizanokane.domain.getActiveCurrencies
import com.mizoar.mizanokane.domain.getCurrentYearMonthString
import com.mizoar.mizanokane.domain.getDailyChartData
import com.mizoar.mizanokane.domain.getDonutChartData
import com.mizoar.mizanokane.domain.getMonthlyChartData
import com.mizoar.mizanokane.domain.getYearlyChartData
import com.mizoar.mizanokane.domain.shiftMonth
import com.mizoar.mizanokane.domain.shiftYear
import com.mizoar.mizanokane.ui.CategoryIcons
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
    val categories by repository.getCategoriesFlow().collectAsState(initial = null)
    val settings by repository.getSettingsFlow().collectAsState(initial = null)

    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (settings?.theme) {
        "dark" -> true
        "light" -> false
        else -> isSystemDark
    }

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

    var currentDonutType by remember { mutableStateOf(StatsChartType.EXPENSE) }
    var activeDonutCategory by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedStatsMonth, selectedCurrency, currentDonutType) {
        activeDonutCategory = null
    }

    val donutData = remember(
        transactions,
        wallets,
        selectedStatsMonth,
        currentScale,
        currentDonutType,
        selectedCurrency,
        isDark
    ) {
        getDonutChartData(
            transactions = transactions,
            wallets = wallets,
            selectedStatsMonth = selectedStatsMonth,
            currentScale = currentScale,
            currentDonutType = currentDonutType,
            currentStatsCurrency = selectedCurrency,
            isDark = isDark
        )
    }

    LaunchedEffect(donutData) {
        if (activeDonutCategory != null && donutData?.items?.none { it.category == activeDonutCategory } == true) {
            activeDonutCategory = null
        }
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

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Kartu Bagian Diagram Donut (Kotlin 6B)
        StatistikDonutSection(
            data = donutData,
            categories = categories,
            currentDonutType = currentDonutType,
            onDonutTypeChange = { currentDonutType = it },
            currentScale = currentScale,
            selectedCurrency = selectedCurrency,
            activeCategory = activeDonutCategory,
            onCategoryToggled = { activeDonutCategory = it },
            onNavigateToTransactionForm = onNavigateToTransactionForm
        )

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

@Composable
fun StatistikDonutSection(
    data: DonutChartData?,
    categories: CategoriesData?,
    currentDonutType: StatsChartType,
    onDonutTypeChange: (StatsChartType) -> Unit,
    currentScale: StatsScale,
    selectedCurrency: String,
    activeCategory: String?,
    onCategoryToggled: (String?) -> Unit,
    onNavigateToTransactionForm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIncome = currentDonutType == StatsChartType.INCOME
    val activeItem = remember(data, activeCategory) {
        data?.items?.find { it.category == activeCategory }
    }

    Box(
        modifier = modifier
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
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null
            ) {
                if (activeCategory != null) {
                    onCategoryToggled(null)
                }
            }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Donut: Judul & Toggle Pengeluaran / Pemasukan
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isIncome) {
                        stringResource(id = R.string.title_donut_pemasukan)
                    } else {
                        stringResource(id = R.string.title_donut_pengeluaran)
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
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
                    TypeToggleButton(
                        text = stringResource(id = R.string.label_pengeluaran),
                        isSelected = !isIncome,
                        activeColor = MizanTheme.colors.expense,
                        onClick = {
                            if (isIncome) {
                                onDonutTypeChange(StatsChartType.EXPENSE)
                            }
                        }
                    )
                    TypeToggleButton(
                        text = stringResource(id = R.string.label_pemasukan),
                        isSelected = isIncome,
                        activeColor = MizanTheme.colors.accent,
                        onClick = {
                            if (!isIncome) {
                                onDonutTypeChange(StatsChartType.INCOME)
                            }
                        }
                    )
                }
            }

            if (data == null) {
                // Keadaan kosong (Empty State)
                val typeLabel = if (isIncome) {
                    stringResource(id = R.string.donut_type_pemasukan)
                } else {
                    stringResource(id = R.string.donut_type_pengeluaran)
                }
                val periodLabel = if (currentScale == StatsScale.YEARLY) {
                    stringResource(id = R.string.donut_period_tahun_ini)
                } else {
                    stringResource(id = R.string.donut_period_bulan_ini)
                }

                Spacer(modifier = Modifier.height(14.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.empty_donut_message, typeLabel, periodLabel),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onNavigateToTransactionForm,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MizanTheme.colors.accent
                        ),
                        border = BorderStroke(1.dp, MizanTheme.colors.accent),
                        modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.action_tambah_transaksi),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MizanTheme.colors.accent,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            } else {
                // Detail Card (saat potongan atau legenda disentuh)
                if (activeItem != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    DonutDetailCard(
                        item = activeItem,
                        categories = categories,
                        donutType = currentDonutType,
                        currency = selectedCurrency
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Diagram Donut Canvas
                val typeLabel = if (isIncome) {
                    stringResource(id = R.string.donut_type_pemasukan)
                } else {
                    stringResource(id = R.string.donut_type_pengeluaran)
                }

                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .align(Alignment.CenterHorizontally)
                        .semantics {
                            contentDescription = "Diagram donut per kategori untuk $typeLabel"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    DonutChartCanvas(
                        data = data,
                        activeCategory = activeCategory,
                        onCategoryToggled = onCategoryToggled
                    )

                    // Teks Tengah (Total / Kategori, Nominal, Persen / Jumlah Kategori)
                    val centerTitle = if (activeItem != null) {
                        if (activeItem.category.length > 13) {
                            activeItem.category.take(11) + "…"
                        } else {
                            activeItem.category
                        }
                    } else {
                        stringResource(id = R.string.donut_center_total)
                    }

                    val centerValue = if (activeItem != null) {
                        Uang.formatCompactCurrency(activeItem.amount, selectedCurrency)
                    } else {
                        Uang.formatCompactCurrency(data.totalAmount, selectedCurrency)
                    }

                    val centerSub = if (activeItem != null) {
                        activeItem.percentageFormatted
                    } else {
                        stringResource(id = R.string.donut_center_kategori_count, data.totalCategoriesCount)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .widthIn(max = 110.dp)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = centerTitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                color = MizanTheme.colors.textSecondary
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = centerValue,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MizanTheme.colors.text
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = centerSub,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MizanTheme.colors.textSecondary
                            ),
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Legenda Donut
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (catItem in data.items) {
                        DonutLegendItemRow(
                            item = catItem,
                            isSelected = activeCategory == catItem.category,
                            categories = categories,
                            donutType = currentDonutType,
                            currency = selectedCurrency,
                            onClick = {
                                onCategoryToggled(if (activeCategory == catItem.category) null else catItem.category)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DonutChartCanvas(
    data: DonutChartData,
    activeCategory: String?,
    onCategoryToggled: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val ringBorderColor = MizanTheme.colors.border

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(data, activeCategory) {
                detectTapGestures { offset ->
                    val centerX = size.width / 2f
                    val centerY = size.height / 2f
                    val dx = offset.x - centerX
                    val dy = offset.y - centerY
                    val dist = kotlin.math.hypot(dx, dy)

                    val rPx = with(density) { 75.dp.toPx() }
                    val wPx = with(density) { 34.dp.toPx() }
                    val innerR = rPx - wPx / 2f - with(density) { 8.dp.toPx() }
                    val outerR = rPx + wPx / 2f + with(density) { 8.dp.toPx() }

                    if (dist in innerR..outerR) {
                        val angleRad = kotlin.math.atan2(dy.toDouble(), dx.toDouble())
                        val angleDeg = Math.toDegrees(angleRad).toFloat()
                        var normalized = (angleDeg + 90f) % 360f
                        if (normalized < 0f) normalized += 360f

                        var currentNorm = 0f
                        var clickedCategory: String? = null
                        for (item in data.items) {
                            val fullSweep = if (data.totalAmount > 0L) {
                                (item.amount.toFloat() / data.totalAmount.toFloat()) * 360f
                            } else 0f

                            if (normalized >= currentNorm && normalized < currentNorm + fullSweep) {
                                clickedCategory = item.category
                                break
                            }
                            currentNorm += fullSweep
                        }

                        if (clickedCategory != null) {
                            onCategoryToggled(if (activeCategory == clickedCategory) null else clickedCategory)
                        } else {
                            if (activeCategory != null) onCategoryToggled(null)
                        }
                    } else {
                        if (activeCategory != null) {
                            onCategoryToggled(null)
                        }
                    }
                }
            }
    ) {
        val diameter = 150.dp.toPx() // 2 * 75.dp
        val arcSize = Size(diameter, diameter)
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val hasActive = activeCategory != null

        // Cincin latar belakang
        drawArc(
            color = ringBorderColor.copy(alpha = 0.35f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = 34.dp.toPx(), cap = StrokeCap.Butt)
        )

        // Potongan-potongan donut
        for (item in data.items) {
            val isSelected = activeCategory == item.category
            val arcAlpha = if (hasActive) {
                if (isSelected) 1.0f else 0.45f
            } else 1.0f

            val strokeWidthPx = (if (isSelected) 40.dp else 34.dp).toPx()

            drawArc(
                color = item.color.copy(alpha = arcAlpha),
                startAngle = item.startAngle,
                sweepAngle = item.sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt)
            )
        }
    }
}

@Composable
private fun DonutDetailCard(
    item: DonutCategoryItem,
    categories: CategoriesData?,
    donutType: StatsChartType,
    currency: String,
    modifier: Modifier = Modifier
) {
    val typeStr = if (donutType == StatsChartType.INCOME) "income" else "expense"
    val iconRes = CategoryIcons.getCategoryIconRes(item.category, typeStr, categories)
    val amountColor = if (donutType == StatsChartType.INCOME) MizanTheme.colors.accent else MizanTheme.colors.expense

    Column(
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
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        // Header row: Icon, CategoryName:, Amount, (Percentage - Count transaksi)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MizanTheme.colors.textSecondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "${item.category}:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.5.sp,
                    color = MizanTheme.colors.text
                )
            )
            Text(
                text = Uang.formatCurrency(item.amount, currency),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = amountColor
                )
            )
            Text(
                text = stringResource(
                    id = R.string.donut_detail_count_format,
                    item.percentageFormatted,
                    item.count
                ),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.5.sp,
                    color = MizanTheme.colors.textSecondary
                )
            )
        }

        // Subcategory section (jika ada sub kategori)
        if (item.detail.subcategories.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MizanTheme.colors.border)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.donut_subcat_section_title),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.textSecondary
                ),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                for (sub in item.detail.subcategories) {
                    val subIconRes = if (sub.isNoSubcategory) {
                        CategoryIcons.getCategoryIconRes(item.category, typeStr, categories)
                    } else {
                        CategoryIcons.getSubcategoryIconRes(item.category, sub.subName, typeStr, categories)
                    }
                    val subDisplayName = if (sub.isNoSubcategory) {
                        stringResource(id = R.string.donut_tanpa_subkategori)
                    } else {
                        sub.subName
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Icon(
                                painter = painterResource(id = subIconRes),
                                contentDescription = null,
                                tint = MizanTheme.colors.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = subDisplayName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MizanTheme.colors.text
                                )
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.donut_subcat_count_format,
                                    sub.percentFormatted,
                                    sub.count
                                ),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                            Text(
                                text = Uang.formatCurrency(sub.amount, currency),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MizanTheme.colors.text
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DonutLegendItemRow(
    item: DonutCategoryItem,
    isSelected: Boolean,
    categories: CategoriesData?,
    donutType: StatsChartType,
    currency: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typeStr = if (donutType == StatsChartType.INCOME) "income" else "expense"
    val iconRes = CategoryIcons.getCategoryIconRes(item.category, typeStr, categories)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MizanTheme.colors.surfaceRaised)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) MizanTheme.colors.textSecondary else MizanTheme.colors.border,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(item.color)
            )
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MizanTheme.colors.textSecondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = item.category,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = MizanTheme.colors.text
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.percentageFormatted,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.textSecondary
                )
            )
            Text(
                text = Uang.formatCurrency(item.amount, currency),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MizanTheme.colors.text
                )
            )
        }
    }
}

