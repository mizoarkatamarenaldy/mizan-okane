package com.mizoar.mizanokane.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.ui.components.BudgetItemView
import com.mizoar.mizanokane.ui.components.MonthSelector
import com.mizoar.mizanokane.ui.components.TransactionItemRow
import com.mizoar.mizanokane.ui.components.WalletBalanceItem
import com.mizoar.mizanokane.domain.calculateWalletBalances
import com.mizoar.mizanokane.domain.formatMonthYear
import com.mizoar.mizanokane.domain.formatMonthYearShort
import com.mizoar.mizanokane.domain.getActiveCurrencies
import com.mizoar.mizanokane.domain.getActiveWallets
import com.mizoar.mizanokane.domain.getBerandaMonthSummary
import com.mizoar.mizanokane.domain.getBudgetSectionSummary
import com.mizoar.mizanokane.domain.getCurrentYearMonthString
import com.mizoar.mizanokane.domain.shiftMonth
import com.mizoar.mizanokane.ui.theme.MizanTheme
import kotlinx.coroutines.launch

@Composable
fun BerandaScreen(
    repository: MizanRepository,
    selectedMonth: String,
    onMonthChange: (String) -> Unit,
    selectedWalletFilter: String,
    onWalletFilterSelect: (String) -> Unit,
    onAddTransactionClick: () -> Unit,
    onEditTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val transactions by repository.getAllTransactionsFlow().collectAsState(initial = emptyList())
    val wallets by repository.getAllWalletsFlow().collectAsState(initial = emptyList())
    val categories by repository.getCategoriesFlow().collectAsState(initial = com.mizoar.mizanokane.data.DefaultData.DEFAULT_CATEGORIES)
    val budgets by repository.getBudgetsFlow().collectAsState(initial = com.mizoar.mizanokane.data.model.BudgetsData())
    val settings by repository.getSettingsFlow().collectAsState(initial = com.mizoar.mizanokane.data.DefaultData.DEFAULT_SETTINGS)

    val currentMonth = remember { getCurrentYearMonthString() }
    val isNextMonthEnabled = selectedMonth < currentMonth

    val activeWallets = remember(wallets) { getActiveWallets(wallets) }
    val activeCurrencies = remember(wallets) { getActiveCurrencies(wallets) }

    val berandaSummary = remember(transactions, selectedMonth, activeCurrencies, wallets) {
        getBerandaMonthSummary(transactions, selectedMonth, activeCurrencies, wallets)
    }

    val walletBalances = remember(transactions, wallets) {
        calculateWalletBalances(transactions, wallets)
    }

    val budgetSectionSummary = remember(budgets, categories, transactions, selectedMonth, wallets) {
        getBudgetSectionSummary(budgets, categories, transactions, selectedMonth, wallets)
    }

    val recentTransactions = remember(transactions) {
        transactions.filter { !it.deleted }
            .sortedWith { a, b ->
                val dateComp = b.date.compareTo(a.date)
                if (dateComp != 0) dateComp
                else {
                    val timeComp = (b.time ?: "").compareTo(a.time ?: "")
                    if (timeComp != 0) timeComp else b.createdAt.compareTo(a.createdAt)
                }
            }
            .take(5)
    }

    var txToDelete by remember { mutableStateOf<TransactionEntity?>(null) }

    if (txToDelete != null) {
        val target = txToDelete!!
        AlertDialog(
            onDismissRequest = { txToDelete = null },
            containerColor = MizanTheme.colors.surface,
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_delete_title),
                    style = MaterialTheme.typography.titleMedium.copy(color = MizanTheme.colors.text)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.confirm_delete_message),
                    style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            repository.markTransactionDeleted(target.id)
                        }
                        txToDelete = null
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.action_hapus),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MizanTheme.colors.expense,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { txToDelete = null }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                    )
                }
            }
        )
    }

    // Aturan: Beranda tidak menampilkan data bila belum ada dompet, sama seperti perilaku web
    if (activeWallets.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Belum ada dompet.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MizanTheme.colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            )
        }
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // === KARTU SALDO HERO ===
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Navigasi bulan
                    MonthSelector(
                        monthLabel = formatMonthYear(selectedMonth),
                        isNextEnabled = isNextMonthEnabled,
                        onPreviousClick = {
                            val prev = shiftMonth(selectedMonth, -1, currentMonth)
                            onMonthChange(prev)
                        },
                        onNextClick = {
                            val next = shiftMonth(selectedMonth, 1, currentMonth)
                            onMonthChange(next)
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.title_total_saldo),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MizanTheme.colors.textSecondary,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Angka Total Saldo Hero
                    for (c in activeCurrencies) {
                        val bal = berandaSummary.totalBalanceByCurrency[c] ?: 0L
                        Text(
                            text = Uang.formatCurrency(bal, c),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = MizanTheme.colors.text,
                                fontFamily = FontFamily.Default
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Garis Pemisah Tipis
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MizanTheme.colors.border)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ringkasan Pemasukan & Pengeluaran Bulan Ini
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Pemasukan
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_pemasukan),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            for (c in activeCurrencies) {
                                val inc = berandaSummary.incomeByCurrency[c] ?: 0L
                                Text(
                                    text = "+${Uang.formatCurrency(inc, c)}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MizanTheme.colors.accent,
                                        fontSize = 17.sp,
                                        fontFamily = FontFamily.Default
                                    )
                                )
                            }
                        }

                        // Garis vertikal
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(MizanTheme.colors.border)
                        )

                        // Pengeluaran
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_pengeluaran),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            for (c in activeCurrencies) {
                                val exp = berandaSummary.expenseByCurrency[c] ?: 0L
                                Text(
                                    text = "−${Uang.formatCurrency(exp, c)}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MizanTheme.colors.expense,
                                        fontSize = 17.sp,
                                        fontFamily = FontFamily.Default
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // === DERETAN DOMPET (Hanya tampil jika dompet >= 2) ===
            if (activeWallets.size >= 2) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (w in activeWallets) {
                        val bal = walletBalances[w.id] ?: 0L
                        val isSelected = selectedWalletFilter == w.id
                        WalletBalanceItem(
                            wallet = w,
                            balanceCents = bal,
                            isSelected = isSelected,
                            onClick = {
                                onWalletFilterSelect(w.id)
                            }
                        )
                    }
                }
            }

            // === BAGIAN BUDGET (Hanya tampil jika ada minimal satu budget tersimpan) ===
            if (budgetSectionSummary != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MizanTheme.colors.surface)
                        .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_budget),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MizanTheme.colors.text
                                )
                            )
                            Text(
                                text = formatMonthYearShort(selectedMonth),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        for (item in budgetSectionSummary.items) {
                            BudgetItemView(item = item)
                        }
                    }
                }
            }

            // === TRANSAKSI TERAKHIR (Maks 5 item) ===
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.label_transaksi_terakhir),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MizanTheme.colors.text
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (recentTransactions.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.empty_beranda_recent),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = onAddTransactionClick,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.action_tambah_transaksi),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MizanTheme.colors.accent
                                    )
                                )
                            }
                        }
                    } else {
                        for (tx in recentTransactions) {
                            TransactionItemRow(
                                tx = tx,
                                wallets = wallets,
                                categories = categories,
                                is24Hour = settings.timeFormat != "12",
                                onEditClick = { onEditTransactionClick(it.id) },
                                onDeleteClick = { txToDelete = it }
                            )
                        }
                    }
                }
            }

            // Space at bottom so content is not behind FAB
            Spacer(modifier = Modifier.height(80.dp))
        }

        // Floating Action Button (+)
        FloatingActionButton(
            onClick = onAddTransactionClick,
            shape = CircleShape,
            containerColor = MizanTheme.colors.accent,
            contentColor = MizanTheme.colors.onAccent,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = stringResource(id = R.string.title_tambah_transaksi),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
