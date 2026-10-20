package com.mizoar.mizanokane.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.domain.formatDateDisplay
import com.mizoar.mizanokane.domain.formatTimeDisplay
import com.mizoar.mizanokane.domain.getActiveWallets
import com.mizoar.mizanokane.domain.getCurrentDateString
import com.mizoar.mizanokane.domain.getCurrentTimeString
import com.mizoar.mizanokane.domain.getNoteSuggestions
import com.mizoar.mizanokane.ui.components.CustomDropdown
import com.mizoar.mizanokane.ui.components.DropdownOption
import com.mizoar.mizanokane.ui.theme.MizanTheme
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionFormScreen(
    repository: MizanRepository,
    txId: String?,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val transactions by repository.getAllTransactionsFlow().collectAsState(initial = emptyList())
    val wallets by repository.getAllWalletsFlow().collectAsState(initial = emptyList())
    val categories by repository.getCategoriesFlow().collectAsState(initial = DefaultData.DEFAULT_CATEGORIES)
    val settings by repository.getSettingsFlow().collectAsState(initial = DefaultData.DEFAULT_SETTINGS)

    val isEditMode = !txId.isNullOrBlank()
    val activeWallets = remember(wallets) { getActiveWallets(wallets) }

    // Form fields
    var txType by remember { mutableStateOf("expense") } // "expense" or "income"
    var amountText by remember { mutableStateOf("") }
    var selectedWalletId by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedSubcategory by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf(getCurrentDateString()) }
    var selectedTime by remember { mutableStateOf(getCurrentTimeString()) }
    var noteText by remember { mutableStateOf("") }
    var originalCreatedAt by remember { mutableStateOf<Long?>(null) }
    var isDummy by remember { mutableStateOf(false) }
    var isLoaded by remember { mutableStateOf(false) }

    // Dialog hapus konfirmasi
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Inisialisasi awal saat mode edit atau tambah baru
    LaunchedEffect(txId, transactions, wallets) {
        if (isLoaded) return@LaunchedEffect
        if (isEditMode) {
            val existing = transactions.find { it.id == txId }
            if (existing != null) {
                txType = if (existing.type == "income") "income" else "expense"
                selectedWalletId = existing.walletId ?: DefaultData.DEFAULT_WALLET_ID
                val curr = Uang.getWalletCurrency(selectedWalletId, wallets)
                amountText = Uang.formatAmountForInput(existing.amount, curr)
                selectedCategory = existing.category ?: ""
                selectedSubcategory = existing.subcategory?.takeIf { it.isNotBlank() }
                selectedDate = existing.date
                selectedTime = existing.time ?: getCurrentTimeString()
                noteText = existing.note ?: ""
                originalCreatedAt = existing.createdAt
                isDummy = existing.dummy
                isLoaded = true
            }
        } else {
            // Mode tambah baru:
            // Sesuai spek: kolom dompet awalnya kosong. Kalau cuma punya 1 dompet, langsung terpilih.
            if (activeWallets.size == 1) {
                selectedWalletId = activeWallets[0].id
            } else {
                selectedWalletId = ""
            }
            selectedDate = getCurrentDateString()
            selectedTime = getCurrentTimeString()
            isLoaded = true
        }
    }

    val currentCurrency = remember(selectedWalletId, wallets) {
        Uang.getWalletCurrency(selectedWalletId.takeIf { it.isNotBlank() }, wallets)
    }

    // Pilihan Dompet
    val walletOptions = remember(activeWallets) {
        activeWallets.map { w ->
            val curr = w.currency.ifBlank { "IDR" }
            DropdownOption(w.id, "${w.name} ($curr)")
        }
    }

    // Pilihan Kategori sesuai tipe aktif
    val categoryList = remember(txType, categories) {
        if (txType == "income") categories.income.map { it.name }
        else categories.expense.map { it.name }
    }
    val categoryOptions = remember(categoryList) {
        categoryList.map { DropdownOption(it, it) }
    }

    // Pilihan Sub Kategori untuk kategori terpilih
    val subcategoryList = remember(txType, selectedCategory, categories) {
        if (selectedCategory.isBlank()) emptyList()
        else {
            val group = if (txType == "income") categories.subcategories.income else categories.subcategories.expense
            group[selectedCategory]?.map { it.name } ?: emptyList()
        }
    }

    // Saran Catatan Lama (dihitung murni lewat fungsi domain saat query >= 2 huruf)
    val noteSuggestions = remember(transactions, noteText) {
        getNoteSuggestions(transactions, noteText)
    }

    // Validasi form untuk tombol Simpan
    val parsedAmountCents = remember(amountText, currentCurrency) {
        Uang.parseAmountInput(amountText, currentCurrency)
    }
    val isFormValid = parsedAmountCents > 0L &&
            selectedWalletId.isNotBlank() &&
            selectedDate.isNotBlank() &&
            selectedTime.isNotBlank()

    if (showDeleteConfirmDialog && txId != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
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
                            repository.markTransactionDeleted(txId)
                            showDeleteConfirmDialog = false
                            onNavigateBack()
                        }
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
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                    )
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MizanTheme.colors.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // === BAR ATAS (HEADER TIPIS: TOMBOL KEMBALI, JUDUL, TOMBOL HAPUS) ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MizanTheme.colors.surface)
                .border(width = 0.5.dp, color = MizanTheme.colors.border)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.action_batal),
                        tint = MizanTheme.colors.text
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (isEditMode) stringResource(id = R.string.title_edit_transaksi)
                    else stringResource(id = R.string.title_tambah_transaksi),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
            }

            if (isEditMode) {
                IconButton(
                    onClick = { showDeleteConfirmDialog = true },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.action_hapus),
                        tint = MizanTheme.colors.expense
                    )
                }
            }
        }

        // === TAB TIPE TRANSAKSI (PENGELUARAN & PEMASUKAN) ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MizanTheme.colors.surfaceRaised)
                .padding(4.dp)
        ) {
            // Tab Pengeluaran
            val isExpense = txType == "expense"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isExpense) MizanTheme.colors.surface else MizanTheme.colors.surfaceRaised)
                    .clickable {
                        txType = "expense"
                        selectedCategory = ""
                        selectedSubcategory = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.label_pengeluaran),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isExpense) FontWeight.Bold else FontWeight.Normal,
                        color = if (isExpense) MizanTheme.colors.expense else MizanTheme.colors.textSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Tab Pemasukan
            val isIncome = txType == "income"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isIncome) MizanTheme.colors.surface else MizanTheme.colors.surfaceRaised)
                    .clickable {
                        txType = "income"
                        selectedCategory = ""
                        selectedSubcategory = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.label_pemasukan),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isIncome) FontWeight.Bold else FontWeight.Normal,
                        color = if (isIncome) MizanTheme.colors.accent else MizanTheme.colors.textSecondary
                    )
                )
            }
        }

        // === ISI FORMULIR (SCROLLABLE) ===
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // 1. JUMLAH
            Text(
                text = stringResource(id = R.string.label_jumlah),
                style = MaterialTheme.typography.labelSmall.copy(color = MizanTheme.colors.textSecondary)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (currentCurrency) {
                        "USD" -> "$ "
                        "JPY" -> "¥ "
                        else -> "Rp "
                    },
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MizanTheme.colors.textSecondary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                BasicTextField(
                    value = amountText,
                    onValueChange = { input ->
                        if (currentCurrency == "USD") {
                            // USD: Izinkan digit, satu koma/titik, dan maksimal 2 desimal
                            val clean = input.filter { it.isDigit() || it == ',' || it == '.' }
                            val sepIdx = clean.indexOfFirst { it == ',' || it == '.' }
                            if (sepIdx != -1) {
                                val intP = clean.substring(0, sepIdx).filter { it.isDigit() }
                                val decP = clean.substring(sepIdx + 1).filter { it.isDigit() }.take(2)
                                val intFormatted = if (intP.isNotEmpty()) Uang.formatNumber(intP.toLongOrNull() ?: 0L) else "0"
                                amountText = "$intFormatted,$decP"
                            } else {
                                val digits = clean.filter { it.isDigit() }
                                amountText = if (digits.isNotEmpty()) Uang.formatNumber(digits.toLongOrNull() ?: 0L) else ""
                            }
                        } else {
                            // IDR / JPY: Hanya digit, format dengan titik pemisah ribuan
                            val digits = input.filter { it.isDigit() }
                            amountText = if (digits.isNotEmpty()) {
                                Uang.formatNumber(digits.toLongOrNull() ?: 0L)
                            } else {
                                ""
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MizanTheme.colors.text
                    ),
                    cursorBrush = SolidColor(MizanTheme.colors.accent),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (currentCurrency == "USD") KeyboardType.Decimal else KeyboardType.Number
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (amountText.isEmpty()) {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = MizanTheme.colors.textSecondary,
                                    fontSize = 20.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. DOMPET (Wajib, Dropdown Kustom)
            Text(
                text = "${stringResource(id = R.string.label_dompet)} *",
                style = MaterialTheme.typography.labelSmall.copy(color = MizanTheme.colors.textSecondary)
            )
            Spacer(modifier = Modifier.height(4.dp))
            CustomDropdown(
                selectedId = selectedWalletId.takeIf { it.isNotBlank() },
                options = walletOptions,
                placeholder = stringResource(id = R.string.placeholder_pilih_dompet),
                onOptionSelected = { id ->
                    selectedWalletId = id
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. KATEGORI (Opsional, Dropdown Kustom)
            Text(
                text = stringResource(id = R.string.label_kategori),
                style = MaterialTheme.typography.labelSmall.copy(color = MizanTheme.colors.textSecondary)
            )
            Spacer(modifier = Modifier.height(4.dp))
            CustomDropdown(
                selectedId = selectedCategory.takeIf { it.isNotBlank() },
                options = categoryOptions,
                placeholder = stringResource(id = R.string.placeholder_pilih_kategori),
                onOptionSelected = { cat ->
                    selectedCategory = cat
                    selectedSubcategory = null
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 4. SUB KATEGORI (Hanya jika kategori memiliki sub kategori)
            if (subcategoryList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(id = R.string.label_subkategori),
                    style = MaterialTheme.typography.labelSmall.copy(color = MizanTheme.colors.textSecondary)
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (sub in subcategoryList) {
                        val isSubSelected = selectedSubcategory == sub
                        Box(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 36.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isSubSelected) MizanTheme.colors.accent.copy(alpha = 0.2f)
                                    else MizanTheme.colors.surface
                                )
                                .border(
                                    1.dp,
                                    if (isSubSelected) MizanTheme.colors.accent else MizanTheme.colors.border,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    selectedSubcategory = if (isSubSelected) null else sub
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = sub,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 13.sp,
                                    fontWeight = if (isSubSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSubSelected) MizanTheme.colors.accent else MizanTheme.colors.text
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 5. TANGGAL & WAKTU (Berdampingan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tanggal
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${stringResource(id = R.string.label_tanggal)} *",
                        style = MaterialTheme.typography.labelSmall.copy(color = MizanTheme.colors.textSecondary)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 44.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MizanTheme.colors.surface)
                            .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                            .clickable {
                                val parts = selectedDate.split('-')
                                val y = parts.getOrNull(0)?.toIntOrNull() ?: 2026
                                val m = (parts.getOrNull(1)?.toIntOrNull() ?: 10) - 1
                                val d = parts.getOrNull(2)?.toIntOrNull() ?: 20
                                DatePickerDialog(context, { _, year, month, day ->
                                    selectedDate = String.format(Locale.ROOT, "%04d-%02d-%02d", year, month + 1, day)
                                }, y, m, d).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = formatDateDisplay(selectedDate),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MizanTheme.colors.text,
                                fontSize = 14.sp
                            )
                        )
                    }
                }

                // Waktu
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${stringResource(id = R.string.label_waktu)} *",
                        style = MaterialTheme.typography.labelSmall.copy(color = MizanTheme.colors.textSecondary)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val is24Hour = settings.timeFormat != "12"
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 44.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MizanTheme.colors.surface)
                            .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                            .clickable {
                                val parts = selectedTime.split(':')
                                val h = parts.getOrNull(0)?.toIntOrNull() ?: 12
                                val min = parts.getOrNull(1)?.toIntOrNull() ?: 0
                                TimePickerDialog(context, { _, hourOfDay, minute ->
                                    selectedTime = String.format(Locale.ROOT, "%02d:%02d", hourOfDay, minute)
                                }, h, min, is24Hour).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = formatTimeDisplay(selectedTime, is24Hour),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MizanTheme.colors.text,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. CATATAN & SARAN CATATAN LAMA
            Text(
                text = stringResource(id = R.string.label_catatan),
                style = MaterialTheme.typography.labelSmall.copy(color = MizanTheme.colors.textSecondary)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 72.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                    .padding(12.dp)
            ) {
                BasicTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 14.sp,
                        color = MizanTheme.colors.text
                    ),
                    cursorBrush = SolidColor(MizanTheme.colors.accent),
                    minLines = 3,
                    maxLines = 5,
                    decorationBox = { innerTextField ->
                        if (noteText.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.placeholder_note),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MizanTheme.colors.textSecondary,
                                    fontSize = 14.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                )
            }

            // Panel Saran Catatan Lama (Di bawah field Catatan)
            if (noteSuggestions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MizanTheme.colors.surface)
                        .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                ) {
                    val typedLower = noteText.trim().lowercase(Locale.ROOT)
                    for (suggestion in noteSuggestions) {
                        val text = suggestion.text
                        val textLower = text.lowercase(Locale.ROOT)
                        val matchIdx = textLower.indexOf(typedLower)

                        val annotated = buildAnnotatedString {
                            if (matchIdx != -1) {
                                append(text.substring(0, matchIdx))
                                withStyle(SpanStyle(color = MizanTheme.colors.accent, fontWeight = FontWeight.Bold)) {
                                    append(text.substring(matchIdx, matchIdx + typedLower.length))
                                }
                                append(text.substring(matchIdx + typedLower.length))
                            } else {
                                append(text)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 44.dp)
                                .clickable {
                                    noteText = suggestion.text
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = annotated,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    color = MizanTheme.colors.text
                                )
                            )
                        }
                    }
                }
            }

            // Spasi bawah ekstra agar field tidak mepet dengan tombol Simpan
            Spacer(modifier = Modifier.height(24.dp))
        }

        // === TOMBOL SIMPAN (MENEMPEL DI BAWAH LAYAR) ===
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MizanTheme.colors.surface)
                .border(width = 0.5.dp, color = MizanTheme.colors.border)
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    val now = System.currentTimeMillis()
                    val id = txId ?: UUID.randomUUID().toString()
                    val createdAt = originalCreatedAt ?: now

                    val entity = TransactionEntity(
                        id = id,
                        type = txType,
                        amount = parsedAmountCents,
                        date = selectedDate,
                        time = selectedTime,
                        category = selectedCategory.takeIf { it.isNotBlank() },
                        subcategory = selectedSubcategory?.takeIf { it.isNotBlank() },
                        walletId = selectedWalletId,
                        toWalletId = null,
                        note = noteText.trim().takeIf { it.isNotBlank() },
                        createdAt = createdAt,
                        updatedAt = now,
                        deleted = false,
                        dummy = isDummy
                    )

                    coroutineScope.launch {
                        repository.saveTransaction(entity)
                        onNavigateBack()
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MizanTheme.colors.accent,
                    contentColor = MizanTheme.colors.onAccent,
                    disabledContainerColor = MizanTheme.colors.surfaceRaised,
                    disabledContentColor = MizanTheme.colors.textSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.action_simpan),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}
