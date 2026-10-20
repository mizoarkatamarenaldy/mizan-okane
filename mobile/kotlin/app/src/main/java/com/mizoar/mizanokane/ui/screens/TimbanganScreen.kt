package com.mizoar.mizanokane.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.BudgetsData
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.domain.TimbangStatus
import com.mizoar.mizanokane.domain.calculateScaleTiltDy
import com.mizoar.mizanokane.domain.calculateTimbangSebelumBeli
import com.mizoar.mizanokane.domain.calculateWalletBalances
import com.mizoar.mizanokane.domain.formatMonthYearShort
import com.mizoar.mizanokane.domain.getActiveCurrencies
import com.mizoar.mizanokane.domain.getActiveWallets
import com.mizoar.mizanokane.domain.getCurrentDateString
import com.mizoar.mizanokane.domain.getCurrentTimeString
import com.mizoar.mizanokane.domain.getCurrentYearMonthString
import com.mizoar.mizanokane.domain.getTimbanganEquilibriumTotals
import com.mizoar.mizanokane.domain.getTimbanganMonthlyData
import com.mizoar.mizanokane.domain.shiftMonth
import com.mizoar.mizanokane.ui.CategoryIcons
import com.mizoar.mizanokane.ui.components.AdjustBalanceDialog
import com.mizoar.mizanokane.ui.components.AmountInputField
import com.mizoar.mizanokane.ui.components.CustomDropdown
import com.mizoar.mizanokane.ui.components.DropdownOption
import com.mizoar.mizanokane.ui.theme.MizanTheme
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun TimbanganScreen(
    repository: MizanRepository,
    onNavigateToTransactionForm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transactions by repository.getAllTransactionsFlow().collectAsState(initial = emptyList())
    val wallets by repository.getAllWalletsFlow().collectAsState(initial = emptyList())
    val categories by repository.getCategoriesFlow().collectAsState(initial = DefaultData.DEFAULT_CATEGORIES)
    val budgets by repository.getBudgetsFlow().collectAsState(initial = BudgetsData())

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var selectedMonth by remember { mutableStateOf(getCurrentYearMonthString()) }
    val activeWallets = remember(wallets) { getActiveWallets(wallets) }
    val availableCurrencies = remember(wallets) { getActiveCurrencies(wallets) }
    var selectedCurrency by remember { mutableStateOf("IDR") }

    LaunchedEffect(availableCurrencies) {
        if (selectedCurrency !in availableCurrencies) {
            selectedCurrency = "IDR"
        }
    }

    val walletBalances = remember(transactions, wallets) {
        calculateWalletBalances(transactions, wallets)
    }

    // State kalkulator
    var priceText by remember { mutableStateOf("") }
    val matchingWallets = remember(activeWallets, selectedCurrency) {
        activeWallets.filter {
            (it.currency.takeIf { c -> c.isNotBlank() } ?: "IDR") == selectedCurrency
        }
    }

    var selectedWalletId by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(matchingWallets) {
        if (selectedWalletId !in matchingWallets.map { it.id }) {
            selectedWalletId = matchingWallets.find { it.id == DefaultData.DEFAULT_WALLET_ID }?.id
                ?: matchingWallets.firstOrNull()?.id
        }
    }

    val expenseCategories = remember(categories) {
        categories.expense
            .filter { it.name != "Equilibrium" }
            .sortedBy { it.order }
            .map { it.name }
    }

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(expenseCategories) {
        if (selectedCategory !in expenseCategories) {
            selectedCategory = expenseCategories.firstOrNull()
        }
    }

    // Dialog sesuaikan saldo
    var adjustWallet by remember { mutableStateOf<WalletEntity?>(null) }

    // Hitungan Timbangan Bulan Ini
    val monthlyData = remember(transactions, selectedMonth, selectedCurrency, wallets) {
        getTimbanganMonthlyData(transactions, selectedMonth, selectedCurrency, wallets)
    }

    val equilibriumTotals = remember(transactions, selectedMonth, wallets) {
        getTimbanganEquilibriumTotals(transactions, selectedMonth, wallets)
    }

    // Hitungan Kalkulator
    val parsedPrice = remember(priceText, selectedCurrency) {
        if (priceText.isNotBlank()) Uang.parseAmountInput(priceText, selectedCurrency) else 0L
    }

    val calcResult = remember(
        parsedPrice,
        selectedWalletId,
        selectedCategory,
        selectedCurrency,
        selectedMonth,
        wallets,
        walletBalances,
        transactions,
        budgets
    ) {
        calculateTimbangSebelumBeli(
            price = parsedPrice,
            selectedWalletId = selectedWalletId,
            selectedCategory = selectedCategory,
            currency = selectedCurrency,
            targetMonthKey = selectedMonth,
            wallets = wallets,
            walletBalances = walletBalances,
            transactions = transactions,
            budgets = budgets
        )
    }

    // Opsi dropdown kalkulator
    val categoryDropdownOptions = remember(expenseCategories, budgets, selectedCurrency, categories) {
        if (expenseCategories.isEmpty()) {
            listOf(DropdownOption(id = "", label = "Belum ada kategori"))
        } else {
            expenseCategories.map { cat ->
                val hasBudget = (selectedCurrency == "IDR") && ((budgets.items[cat] ?: 0L) > 0L)
                val budgetSuffix = if (hasBudget) {
                    " (Budget: ${Uang.formatCurrency(budgets.items[cat] ?: 0L, "IDR")})"
                } else {
                    ""
                }
                DropdownOption(
                    id = cat,
                    label = "$cat$budgetSuffix",
                    iconRes = CategoryIcons.getCategoryIconRes(cat, categories)
                )
            }
        }
    }

    val walletDropdownOptions = remember(matchingWallets, walletBalances, selectedCurrency) {
        if (matchingWallets.isEmpty()) {
            listOf(DropdownOption(id = "", label = "Belum ada dompet"))
        } else {
            matchingWallets.map { w ->
                val bal = walletBalances[w.id] ?: 0L
                DropdownOption(
                    id = w.id,
                    label = "${w.name} (${Uang.formatCurrency(bal, selectedCurrency)})"
                )
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // === HEADER HALAMAN TIMBANGAN ===
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.title_timbangan_page),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MizanTheme.colors.text
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(id = R.string.desc_timbangan_page),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MizanTheme.colors.textSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // === PILIHAN MATA UANG (disembunyikan bila hanya Rupiah) ===
            if (availableCurrencies.size > 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.label_mata_uang),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MizanTheme.colors.textSecondary
                        )
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MizanTheme.colors.surfaceRaised)
                            .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // Rupiah
                        CurrencyToggleButton(
                            text = stringResource(id = R.string.currency_rupiah),
                            isSelected = selectedCurrency == "IDR",
                            onClick = { selectedCurrency = "IDR" }
                        )
                        // Yen
                        if ("JPY" in availableCurrencies) {
                            CurrencyToggleButton(
                                text = stringResource(id = R.string.currency_yen),
                                isSelected = selectedCurrency == "JPY",
                                onClick = { selectedCurrency = "JPY" }
                            )
                        }
                        // Dolar
                        if ("USD" in availableCurrencies) {
                            CurrencyToggleButton(
                                text = stringResource(id = R.string.currency_dolar),
                                isSelected = selectedCurrency == "USD",
                                onClick = { selectedCurrency = "USD" }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // === BAGIAN 1: TIMBANGAN BULAN INI ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header kartu: Judul dan Navigasi Bulan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.title_timbangan_bulan_ini),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MizanTheme.colors.text
                            )
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    selectedMonth = shiftMonth(selectedMonth, -1, maxMonth = null)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chevron_left),
                                    contentDescription = "Bulan sebelumnya",
                                    tint = MizanTheme.colors.text,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = formatMonthYearShort(selectedMonth),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MizanTheme.colors.text
                                )
                            )
                            IconButton(
                                onClick = {
                                    selectedMonth = shiftMonth(selectedMonth, 1, maxMonth = null)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chevron_right),
                                    contentDescription = "Bulan berikutnya",
                                    tint = MizanTheme.colors.text,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (monthlyData.txCount == 0) {
                        // Kondisi kosong bulan ini
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.empty_timbangan_month),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MizanTheme.colors.textSecondary,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            OutlinedButton(
                                onClick = onNavigateToTransactionForm,
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, MizanTheme.colors.accent),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MizanTheme.colors.accent
                                ),
                                modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.action_tambah_transaksi),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    } else {
                        // Gambar garis timbangan (outline)
                        TimbanganScaleCanvas(
                            income = monthlyData.income,
                            expense = monthlyData.expense,
                            diff = monthlyData.diff,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Kotak jumlah piring kiri (pemasukan) dan kanan (pengeluaran)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = stringResource(id = R.string.label_pemasukan),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MizanTheme.colors.textSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Text(
                                    text = "+" + Uang.formatCurrency(monthlyData.income, selectedCurrency),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MizanTheme.colors.accent
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = stringResource(id = R.string.label_pengeluaran),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MizanTheme.colors.textSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Text(
                                    text = "−" + Uang.formatCurrency(monthlyData.expense, selectedCurrency),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MizanTheme.colors.expense
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Kotak kesimpulan (Surplus / Defisit)
                        val isSurplus = monthlyData.diff >= 0L
                        val verdictBg = if (isSurplus) {
                            MizanTheme.colors.accent.copy(alpha = 0.08f)
                        } else {
                            MizanTheme.colors.expense.copy(alpha = 0.08f)
                        }
                        val verdictBorder = if (isSurplus) {
                            MizanTheme.colors.accent.copy(alpha = 0.35f)
                        } else {
                            MizanTheme.colors.expense.copy(alpha = 0.35f)
                        }
                        val verdictColor = if (isSurplus) MizanTheme.colors.accent else MizanTheme.colors.expense

                        val verdictText = when {
                            monthlyData.diff > 0L -> {
                                stringResource(
                                    id = R.string.label_surplus,
                                    Uang.formatCurrency(monthlyData.diff, selectedCurrency)
                                )
                            }
                            monthlyData.diff < 0L -> {
                                stringResource(
                                    id = R.string.label_defisit,
                                    Uang.formatCurrency(kotlin.math.abs(monthlyData.diff), selectedCurrency)
                                )
                            }
                            else -> {
                                stringResource(
                                    id = R.string.label_surplus,
                                    Uang.formatCurrency(0L, selectedCurrency)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(verdictBg)
                                .border(1.dp, verdictBorder, RoundedCornerShape(6.dp))
                                .padding(vertical = 12.dp, horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = verdictText,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = verdictColor
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Baris terpisah bernama "Equilibrium" di bawah timbangan bulan ini
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MizanTheme.colors.surfaceRaised)
                            .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                            .padding(vertical = 12.dp, horizontal = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f, fill = false)) {
                            Text(
                                text = stringResource(id = R.string.label_equilibrium),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MizanTheme.colors.text
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.desc_equilibrium_bulan_ini),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        if (equilibriumTotals.isEmpty()) {
                            Text(
                                text = Uang.formatCurrency(0L, selectedCurrency),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                equilibriumTotals.forEachIndexed { index, eq ->
                                    if (index > 0) {
                                        Text(
                                            text = " • ",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MizanTheme.colors.textSecondary
                                            )
                                        )
                                    }
                                    val eqColor = when {
                                        eq.total > 0L -> MizanTheme.colors.accent
                                        eq.total < 0L -> MizanTheme.colors.expense
                                        else -> MizanTheme.colors.textSecondary
                                    }
                                    val eqText = when {
                                        eq.total > 0L -> "+" + Uang.formatCurrency(eq.total, eq.currency)
                                        eq.total < 0L -> "−" + Uang.formatCurrency(kotlin.math.abs(eq.total), eq.currency)
                                        else -> Uang.formatCurrency(0L, eq.currency)
                                    }
                                    Text(
                                        text = eqText,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = eqColor
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === BAGIAN 2: KALKULATOR "TIMBANG SEBELUM BELI" ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.title_timbang_sebelum_beli),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MizanTheme.colors.text
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(id = R.string.desc_timbang_sebelum_beli),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MizanTheme.colors.textSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Input Harga Barang
                    val currencyPrefix = when (selectedCurrency) {
                        "USD" -> "$"
                        "JPY" -> "¥"
                        else -> "Rp"
                    }
                    Text(
                        text = stringResource(id = R.string.label_harga_barang, currencyPrefix),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    AmountInputField(
                        amountText = priceText,
                        onAmountChange = { priceText = it },
                        currency = selectedCurrency
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pilihan Kategori Pengeluaran
                    Text(
                        text = stringResource(id = R.string.label_kategori_pengeluaran),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    CustomDropdown(
                        selectedId = selectedCategory,
                        options = categoryDropdownOptions,
                        placeholder = stringResource(id = R.string.empty_kategori_option),
                        onOptionSelected = { selectedCategory = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pilihan Dompet Sumber Dana
                    Text(
                        text = stringResource(id = R.string.label_dompet_sumber_dana),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    CustomDropdown(
                        selectedId = selectedWalletId,
                        options = walletDropdownOptions,
                        placeholder = stringResource(id = R.string.empty_dompet_option),
                        onOptionSelected = { selectedWalletId = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(thickness = 1.dp, color = MizanTheme.colors.border)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Banner Status (Aman / Pas / Berat)
                    val (bannerBg, bannerBorder, badgeBg, badgeColor) = when (calcResult.status) {
                        TimbangStatus.AMAN -> listOf(
                            MizanTheme.colors.accent.copy(alpha = 0.12f),
                            MizanTheme.colors.accent.copy(alpha = 0.32f),
                            MizanTheme.colors.accent.copy(alpha = 0.20f),
                            MizanTheme.colors.accent
                        )
                        TimbangStatus.PAS -> listOf(
                            MizanTheme.colors.budgetWarn.copy(alpha = 0.12f),
                            MizanTheme.colors.budgetWarn.copy(alpha = 0.32f),
                            MizanTheme.colors.budgetWarn.copy(alpha = 0.20f),
                            MizanTheme.colors.budgetWarn
                        )
                        TimbangStatus.BERAT -> listOf(
                            MizanTheme.colors.expense.copy(alpha = 0.12f),
                            MizanTheme.colors.expense.copy(alpha = 0.32f),
                            MizanTheme.colors.expense.copy(alpha = 0.20f),
                            MizanTheme.colors.expense
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(bannerBg)
                            .border(1.dp, bannerBorder, RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.label_status_prefix),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MizanTheme.colors.text
                                    )
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(badgeBg)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = calcResult.statusBadge.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = badgeColor,
                                            letterSpacing = 0.5.sp
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = calcResult.statusDesc,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    color = MizanTheme.colors.textSecondary,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Rincian Hasil
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Saldo dompet setelah beli
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_saldo_setelah_beli),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                            val walletColor = if (calcResult.newWalletBal < 0L) {
                                MizanTheme.colors.expense
                            } else {
                                MizanTheme.colors.text
                            }
                            Text(
                                text = Uang.formatCurrency(calcResult.newWalletBal, selectedCurrency),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = walletColor,
                                    fontFamily = FontFamily.Default
                                )
                            )
                        }

                        // 2. Sisa budget kategori (hanya bila memiliki budget)
                        if (calcResult.hasBudget) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.label_sisa_budget_kategori),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MizanTheme.colors.textSecondary
                                    )
                                )
                                if (calcResult.remainingBudget >= 0L) {
                                    Text(
                                        text = "${Uang.formatCurrency(calcResult.remainingBudget, "IDR")} (${calcResult.remainingPercent}% tersisa)",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MizanTheme.colors.text
                                        )
                                    )
                                } else {
                                    Text(
                                        text = "Lewat Rp " + Uang.formatNumber(kotlin.math.abs(calcResult.remainingBudget) / 100),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MizanTheme.colors.expense
                                        )
                                    )
                                }
                            }
                        }

                        // 3. Timbangan bulan ini setelah beli
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_timbangan_setelah_beli),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                            val scaleText = when {
                                calcResult.newMonthDiff > 0L -> {
                                    "Surplus " + Uang.formatCurrency(calcResult.newMonthDiff, selectedCurrency)
                                }
                                calcResult.newMonthDiff < 0L -> {
                                    "Defisit " + Uang.formatCurrency(kotlin.math.abs(calcResult.newMonthDiff), selectedCurrency)
                                }
                                else -> {
                                    "Surplus " + Uang.formatCurrency(0L, selectedCurrency)
                                }
                            }
                            val scaleColor = if (calcResult.newMonthDiff < 0L) {
                                MizanTheme.colors.expense
                            } else {
                                MizanTheme.colors.text
                            }
                            Text(
                                text = scaleText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = scaleColor
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // === BAGIAN 3: SESUAIKAN SALDO ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.title_sesuaikan_saldo),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MizanTheme.colors.text
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(id = R.string.desc_sesuaikan_saldo),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MizanTheme.colors.textSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (activeWallets.isEmpty()) {
                        Text(
                            text = "Belum ada dompet.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MizanTheme.colors.textSecondary
                            )
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (wallet in activeWallets) {
                                val bal = walletBalances[wallet.id] ?: 0L
                                val curr = wallet.currency.takeIf { it.isNotBlank() } ?: "IDR"

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .defaultMinSize(minHeight = 44.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(MizanTheme.colors.surfaceRaised)
                                        .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = wallet.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = MizanTheme.colors.text
                                            )
                                        )
                                        Text(
                                            text = Uang.formatCurrency(bal, curr),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MizanTheme.colors.textSecondary
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { adjustWallet = wallet },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MizanTheme.colors.surface,
                                            contentColor = MizanTheme.colors.text
                                        ),
                                        border = BorderStroke(1.dp, MizanTheme.colors.border),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                        modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.action_sesuaikan),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 13.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Ruang kosong agar tidak tertutup tombol navigasi bawah
            Spacer(modifier = Modifier.height(80.dp))
        }

        // Dialog sesuaikan saldo
        adjustWallet?.let { targetWallet ->
            AdjustBalanceDialog(
                wallet = targetWallet,
                appBalance = walletBalances[targetWallet.id] ?: 0L,
                onDismissRequest = { adjustWallet = null },
                onConfirm = { subcat, diffCents ->
                    coroutineScope.launch {
                        try {
                            val now = System.currentTimeMillis()
                            val tx = TransactionEntity(
                                id = UUID.randomUUID().toString(),
                                type = "penyesuaian",
                                amount = diffCents,
                                date = getCurrentDateString(),
                                time = getCurrentTimeString(),
                                category = "Equilibrium",
                                subcategory = subcat,
                                walletId = targetWallet.id,
                                toWalletId = null,
                                note = "",
                                createdAt = now,
                                updatedAt = now,
                                deleted = false,
                                dummy = false
                            )
                            repository.saveTransaction(tx)
                            adjustWallet = null
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_adjust_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_adjust_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun CurrencyToggleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = 36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) MizanTheme.colors.accent else MizanTheme.colors.surfaceRaised)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MizanTheme.colors.onAccent else MizanTheme.colors.textSecondary
            )
        )
    }
}

@Composable
private fun TimbanganScaleCanvas(
    income: Long,
    expense: Long,
    diff: Long,
    modifier: Modifier = Modifier
) {
    val textColor = MizanTheme.colors.text
    val textSecondaryColor = MizanTheme.colors.textSecondary
    val surfaceColor = MizanTheme.colors.surface

    val dy = remember(income, expense, diff) {
        calculateScaleTiltDy(income, expense, diff)
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        val targetWidth = 320f
        val targetHeight = 180f
        val scale = kotlin.math.min(size.width / targetWidth, size.height / targetHeight)
        val offsetX = (size.width - targetWidth * scale) / 2f
        val offsetY = (size.height - targetHeight * scale) / 2f

        val strokeThick = 2.5f * scale
        val strokeThin = 1.5f * scale
        val strokeMed = 1.8f * scale

        fun sx(x: Float) = offsetX + x * scale
        fun sy(y: Float) = offsetY + y * scale

        val pivotX = 160f
        val pivotY = 42f
        val leftX = 65f
        val leftY = pivotY + dy
        val rightX = 255f
        val rightY = pivotY - dy

        val stringLen = 48f
        val leftSaucerY = leftY + stringLen
        val rightSaucerY = rightY + stringLen

        // Stand dan Tiang Utama
        drawLine(
            color = textColor,
            start = Offset(sx(110f), sy(160f)),
            end = Offset(sx(210f), sy(160f)),
            strokeWidth = strokeThick,
            cap = StrokeCap.Round
        )
        drawLine(
            color = textColor,
            start = Offset(sx(pivotX), sy(160f)),
            end = Offset(sx(pivotX), sy(pivotY)),
            strokeWidth = strokeThick,
            cap = StrokeCap.Round
        )

        // Lengan Timbangan (Beam)
        drawLine(
            color = textColor,
            start = Offset(sx(leftX), sy(leftY)),
            end = Offset(sx(rightX), sy(rightY)),
            strokeWidth = strokeThick,
            cap = StrokeCap.Round
        )

        // Center Pivot
        drawCircle(
            color = surfaceColor,
            radius = 4.5f * scale,
            center = Offset(sx(pivotX), sy(pivotY))
        )
        drawCircle(
            color = textColor,
            radius = 4.5f * scale,
            center = Offset(sx(pivotX), sy(pivotY)),
            style = Stroke(width = 2f * scale)
        )

        // Left Joint
        drawCircle(
            color = surfaceColor,
            radius = 3f * scale,
            center = Offset(sx(leftX), sy(leftY))
        )
        drawCircle(
            color = textColor,
            radius = 3f * scale,
            center = Offset(sx(leftX), sy(leftY)),
            style = Stroke(width = strokeMed)
        )

        // Right Joint
        drawCircle(
            color = surfaceColor,
            radius = 3f * scale,
            center = Offset(sx(rightX), sy(rightY))
        )
        drawCircle(
            color = textColor,
            radius = 3f * scale,
            center = Offset(sx(rightX), sy(rightY)),
            style = Stroke(width = strokeMed)
        )

        // Piring Kiri (Pemasukan)
        drawLine(
            color = textSecondaryColor,
            start = Offset(sx(leftX), sy(leftY)),
            end = Offset(sx(leftX - 25f), sy(leftSaucerY)),
            strokeWidth = strokeThin,
            cap = StrokeCap.Round
        )
        drawLine(
            color = textSecondaryColor,
            start = Offset(sx(leftX), sy(leftY)),
            end = Offset(sx(leftX + 25f), sy(leftSaucerY)),
            strokeWidth = strokeThin,
            cap = StrokeCap.Round
        )
        val leftSaucerPath = Path().apply {
            moveTo(sx(leftX - 28f), sy(leftSaucerY))
            quadraticBezierTo(
                sx(leftX), sy(leftSaucerY + 12f),
                sx(leftX + 28f), sy(leftSaucerY)
            )
        }
        drawPath(
            path = leftSaucerPath,
            color = textColor,
            style = Stroke(width = strokeThick, cap = StrokeCap.Round)
        )

        // Piring Kanan (Pengeluaran)
        drawLine(
            color = textSecondaryColor,
            start = Offset(sx(rightX), sy(rightY)),
            end = Offset(sx(rightX - 25f), sy(rightSaucerY)),
            strokeWidth = strokeThin,
            cap = StrokeCap.Round
        )
        drawLine(
            color = textSecondaryColor,
            start = Offset(sx(rightX), sy(rightY)),
            end = Offset(sx(rightX + 25f), sy(rightSaucerY)),
            strokeWidth = strokeThin,
            cap = StrokeCap.Round
        )
        val rightSaucerPath = Path().apply {
            moveTo(sx(rightX - 28f), sy(rightSaucerY))
            quadraticBezierTo(
                sx(rightX), sy(rightSaucerY + 12f),
                sx(rightX + 28f), sy(rightSaucerY)
            )
        }
        drawPath(
            path = rightSaucerPath,
            color = textColor,
            style = Stroke(width = strokeThick, cap = StrokeCap.Round)
        )
    }
}
