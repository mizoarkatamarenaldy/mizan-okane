package com.mizoar.mizanokane.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.domain.DateGroup
import com.mizoar.mizanokane.domain.filterAndGroupTransactions
import com.mizoar.mizanokane.domain.formatMonthYear
import com.mizoar.mizanokane.domain.getActiveCurrencies
import com.mizoar.mizanokane.domain.getActiveWallets
import com.mizoar.mizanokane.domain.getCurrentYearMonthString
import com.mizoar.mizanokane.domain.getRiwayatSummary
import com.mizoar.mizanokane.domain.shiftMonth
import com.mizoar.mizanokane.ui.components.CustomDropdown
import com.mizoar.mizanokane.ui.components.DropdownOption
import com.mizoar.mizanokane.ui.components.TransactionItemRow
import com.mizoar.mizanokane.ui.theme.MizanTheme
import kotlinx.coroutines.launch

@Composable
fun RiwayatScreen(
    repository: MizanRepository,
    selectedMonth: String,
    onMonthChange: (String) -> Unit,
    selectedWalletFilter: String,
    onWalletFilterChange: (String) -> Unit,
    onAddTransactionClick: () -> Unit,
    onEditTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val transactions by repository.getAllTransactionsFlow().collectAsState(initial = emptyList())
    val wallets by repository.getAllWalletsFlow().collectAsState(initial = emptyList())
    val categories by repository.getCategoriesFlow().collectAsState(initial = com.mizoar.mizanokane.data.DefaultData.DEFAULT_CATEGORIES)
    val settings by repository.getSettingsFlow().collectAsState(initial = com.mizoar.mizanokane.data.DefaultData.DEFAULT_SETTINGS)

    var searchQuery by remember { mutableStateOf("") }
    var isSearchOpen by remember { mutableStateOf(false) }
    var txToDelete by remember { mutableStateOf<TransactionEntity?>(null) }

    val activeWallets = remember(wallets) { getActiveWallets(wallets) }
    val activeCurrencies = remember(wallets) { getActiveCurrencies(wallets) }

    // Hitung ringkasan Riwayat (mengikuti filter bulan, dompet, dan pencarian)
    val riwayatSummary = remember(transactions, selectedMonth, selectedWalletFilter, activeCurrencies, wallets, searchQuery) {
        getRiwayatSummary(transactions, selectedMonth, selectedWalletFilter, activeCurrencies, wallets, searchQuery)
    }

    // Kelompokkan transaksi per tanggal
    val groupedTransactions = remember(transactions, selectedMonth, selectedWalletFilter, searchQuery, wallets) {
        filterAndGroupTransactions(transactions, selectedMonth, selectedWalletFilter, searchQuery, wallets)
    }

    // Pilihan dropdown Dompet
    val walletOptions = remember(activeWallets) {
        val list = mutableListOf(
            DropdownOption("all", "Semua Dompet")
        )
        for (w in activeWallets) {
            val curr = w.currency.ifBlank { "IDR" }
            list.add(DropdownOption(w.id, "${w.name} ($curr)"))
        }
        list
    }

    // Pilihan dropdown Bulan
    val currentMonthKey = remember { getCurrentYearMonthString() }
    val monthOptions = remember(transactions, currentMonthKey) {
        val list = mutableListOf(
            DropdownOption("all", "Semua bulan")
        )
        val monthSet = sortedSetOf(Comparator.reverseOrder<String>())
        monthSet.add(currentMonthKey)
        // Tambahkan bulan mundur 12 bulan terakhir
        var cursorMonth = currentMonthKey
        for (i in 1..11) {
            cursorMonth = shiftMonth(cursorMonth, -1, null)
            monthSet.add(cursorMonth)
        }
        // Tambahkan bulan dari data transaksi yang ada
        for (tx in transactions) {
            if (!tx.deleted && tx.date.length >= 7) {
                monthSet.add(tx.date.take(7))
            }
        }
        for (m in monthSet) {
            list.add(DropdownOption(m, formatMonthYear(m)))
        }
        list
    }

    if (txToDelete != null) {
        val target = txToDelete!!
        val isTransferTarget = target.type == "transfer"
        AlertDialog(
            onDismissRequest = { txToDelete = null },
            containerColor = MizanTheme.colors.surface,
            title = {
                Text(
                    text = if (isTransferTarget) stringResource(id = R.string.confirm_delete_transfer_title)
                    else stringResource(id = R.string.confirm_delete_title),
                    style = MaterialTheme.typography.titleMedium.copy(color = MizanTheme.colors.text)
                )
            },
            text = {
                Text(
                    text = if (isTransferTarget) stringResource(id = R.string.confirm_delete_transfer_message)
                    else stringResource(id = R.string.confirm_delete_message),
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

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // === HEADER: JUDUL RIWAYAT + RINGKASAN + TOMBOL SEARCH ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.nav_riwayat),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MizanTheme.colors.text
                        )
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // Ringkasan Pemasukan & Pengeluaran di samping judul
                    Column {
                        for (c in riwayatSummary.targetCurrencies) {
                            val inc = riwayatSummary.incomeByCurrency[c] ?: 0L
                            val exp = riwayatSummary.expenseByCurrency[c] ?: 0L
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "+${Uang.formatCurrency(inc, c)}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MizanTheme.colors.accent,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = FontFamily.Default,
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "−${Uang.formatCurrency(exp, c)}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MizanTheme.colors.expense,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = FontFamily.Default,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }

                IconButton(
                    onClick = {
                        isSearchOpen = !isSearchOpen
                        if (!isSearchOpen) searchQuery = ""
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = if (isSearchOpen) R.drawable.ic_close else R.drawable.ic_search),
                        contentDescription = "Cari",
                        tint = if (isSearchOpen || searchQuery.isNotBlank()) MizanTheme.colors.accent else MizanTheme.colors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Kolom Pencarian (Bila aktif)
            if (isSearchOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MizanTheme.colors.surface)
                        .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                color = MizanTheme.colors.text,
                                fontSize = 14.sp
                            ),
                            cursorBrush = SolidColor(MizanTheme.colors.accent),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.placeholder_search),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MizanTheme.colors.textSecondary,
                                            fontSize = 14.sp
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        )
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "Hapus teks",
                                    tint = MizanTheme.colors.textSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // === DROPDOWN DOMPET & DROPDOWN BULAN ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomDropdown(
                    selectedId = selectedWalletFilter,
                    options = walletOptions,
                    placeholder = stringResource(id = R.string.label_semua_dompet),
                    onOptionSelected = onWalletFilterChange,
                    modifier = Modifier.weight(1f)
                )

                CustomDropdown(
                    selectedId = selectedMonth,
                    options = monthOptions,
                    placeholder = stringResource(id = R.string.label_semua_bulan),
                    onOptionSelected = onMonthChange,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // === DAFTAR TRANSAKSI DIKELOMPOKKAN PER TANGGAL ===
            if (groupedTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (searchQuery.isNotBlank()) {
                                stringResource(id = R.string.empty_riwayat_search)
                            } else {
                                stringResource(id = R.string.empty_riwayat)
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MizanTheme.colors.textSecondary,
                                textAlign = TextAlign.Center
                            )
                        )
                        if (searchQuery.isBlank()) {
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
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    for (group in groupedTransactions) {
                        item(key = "header_${group.dateKey}") {
                            DateGroupHeader(group = group)
                        }

                        items(
                            items = group.transactions,
                            key = { it.id }
                        ) { tx ->
                            TransactionItemRow(
                                tx = tx,
                                wallets = wallets,
                                categories = categories,
                                is24Hour = settings.timeFormat != "12",
                                onEditClick = { onEditTransactionClick(it.id) },
                                onDeleteClick = { txToDelete = it }
                            )
                        }

                        item(key = "divider_${group.dateKey}") {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }

                    // Padding bawah agar item terakhir tidak tertutup FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
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

@Composable
fun DateGroupHeader(
    group: DateGroup,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = group.title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = MizanTheme.colors.textSecondary,
                fontSize = 13.sp
            )
        )

        val netEntries = group.netByCurrency.entries.toList()
        if (netEntries.isEmpty()) {
            Text(
                text = Uang.formatCurrency(0L, "IDR"),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MizanTheme.colors.textSecondary,
                    fontFamily = FontFamily.Default
                )
            )
        } else if (netEntries.size == 1) {
            val (c, net) = netEntries[0]
            val (sign, color) = when {
                net > 0 -> "+" to MizanTheme.colors.accent
                net < 0 -> "−" to MizanTheme.colors.expense
                else -> "" to MizanTheme.colors.textSecondary
            }
            Text(
                text = "$sign${Uang.formatCurrency(kotlin.math.abs(net), c)}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Default
                )
            )
        } else {
            val text = netEntries.map { (c, net) ->
                val sign = if (net > 0) "+" else if (net < 0) "−" else ""
                "$sign${Uang.formatCurrency(kotlin.math.abs(net), c)}"
            }.joinToString(" • ")
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MizanTheme.colors.textSecondary,
                    fontFamily = FontFamily.Default
                )
            )
        }
    }
}
