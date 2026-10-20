package com.mizoar.mizanokane.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.WalletEntity
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.domain.calculateWalletBalances
import com.mizoar.mizanokane.domain.getActiveWallets
import com.mizoar.mizanokane.ui.components.CustomDropdown
import com.mizoar.mizanokane.ui.components.DropdownOption
import com.mizoar.mizanokane.ui.components.ReorderDragHandle
import com.mizoar.mizanokane.ui.theme.MizanTheme
import java.util.UUID
import kotlinx.coroutines.launch

/**
 * Layar Kelola Dompet aplikasi Mizan Okane.
 * Sesuai dev/PLANSPEC.md item "Kotlin 4A: Dompet, transfer, mata uang":
 * - Daftar dompet aktif dengan nama dan mata uang
 * - Ubah nama dompet secara inline
 * - Hapus dompet (soft delete) dengan konfirmasi saldo sisa dan penjagaan minimal 1 dompet
 * - Geser urutan dompet dengan pegangan ≡ via detectDragGestures (Compose foundation API)
 * - Tambah dompet baru dengan pilihan mata uang (IDR, JPY, USD)
 */
@Composable
fun KelolaDompetScreen(
    repository: MizanRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val wallets by repository.getAllWalletsFlow().collectAsState(initial = emptyList())
    val transactions by repository.getAllTransactionsFlow().collectAsState(initial = emptyList())

    val activeWallets = remember(wallets) { getActiveWallets(wallets) }
    val walletBalances = remember(transactions, wallets) { calculateWalletBalances(transactions, wallets) }

    // State daftar dompet lokal untuk mendukung swap interaktif saat digeser
    var activeWalletsList by remember(activeWallets) { mutableStateOf(activeWallets) }

    // State geser urutan
    var draggingWalletId by remember { mutableStateOf<String?>(null) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    // State inline edit nama dompet
    var editingWalletId by remember { mutableStateOf<String?>(null) }
    var editingNameText by remember { mutableStateOf("") }

    // State tambah dompet baru
    var newWalletName by remember { mutableStateOf("") }
    var newWalletCurrency by remember { mutableStateOf("IDR") }

    // Dialog konfirmasi hapus dompet
    var walletToDelete by remember { mutableStateOf<WalletEntity?>(null) }

    // Sinkronkan daftar lokal bila activeWallets dari repository berubah saat tidak sedang menggeser
    LaunchedEffect(activeWallets) {
        if (draggingWalletId == null) {
            activeWalletsList = activeWallets
        }
    }

    if (walletToDelete != null) {
        val target = walletToDelete!!
        val bal = walletBalances[target.id] ?: 0L
        val curr = target.currency.ifBlank { "IDR" }
        val formattedBal = Uang.formatCurrency(bal, curr)

        AlertDialog(
            onDismissRequest = { walletToDelete = null },
            containerColor = MizanTheme.colors.surface,
            title = {
                Text(
                    text = stringResource(id = R.string.confirm_delete_wallet_title, target.name, formattedBal),
                    style = MaterialTheme.typography.titleMedium.copy(color = MizanTheme.colors.text)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            repository.markWalletDeleted(target.id)
                            walletToDelete = null
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_wallet_deleted, target.name),
                                Toast.LENGTH_SHORT
                            ).show()
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
                TextButton(onClick = { walletToDelete = null }) {
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
        // === HEADER BAR ATAS ===
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MizanTheme.colors.surface)
                .border(width = 0.5.dp, color = MizanTheme.colors.border)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                text = stringResource(id = R.string.title_dompet),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.text
                )
            )
        }

        // === KONTEN KELOLA DOMPET (SCROLLABLE) ===
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Deskripsi kartu
            Text(
                text = stringResource(id = R.string.desc_dompet_card),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MizanTheme.colors.textSecondary,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // === DAFTAR DOMPET ===
            if (activeWalletsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MizanTheme.colors.surface)
                        .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.empty_dompet),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val itemHeightThreshold = with(density) { 56.dp.toPx() }

                    for (wallet in activeWalletsList) {
                        val isEditingThis = editingWalletId == wallet.id
                        val isDraggingThis = draggingWalletId == wallet.id

                        val rowModifier = Modifier
                            .fillMaxWidth()
                            .zIndex(if (isDraggingThis) 2f else 1f)
                            .graphicsLayer {
                                if (isDraggingThis) {
                                    translationY = dragOffsetY
                                    shadowElevation = 8f
                                }
                            }
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDraggingThis) MizanTheme.colors.surfaceRaised else MizanTheme.colors.surface)
                            .border(
                                width = if (isDraggingThis) 1.5.dp else 1.dp,
                                color = if (isDraggingThis) MizanTheme.colors.accent else MizanTheme.colors.border,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)

                        Box(modifier = rowModifier) {
                            if (isEditingThis) {
                                // Mode inline edit nama dompet
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    BasicTextField(
                                        value = editingNameText,
                                        onValueChange = { editingNameText = it },
                                        modifier = Modifier
                                            .weight(1f)
                                            .defaultMinSize(minHeight = 44.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(MizanTheme.colors.surfaceRaised)
                                            .border(1.dp, MizanTheme.colors.accent, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                            color = MizanTheme.colors.text,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        cursorBrush = SolidColor(MizanTheme.colors.accent),
                                        singleLine = true
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    TextButton(
                                        onClick = {
                                            val newName = editingNameText.trim()
                                            if (newName.isEmpty()) {
                                                Toast.makeText(
                                                    context,
                                                    context.getString(R.string.warn_wallet_empty_name),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@TextButton
                                            }
                                            if (newName == wallet.name) {
                                                editingWalletId = null
                                                return@TextButton
                                            }
                                            val exists = activeWallets.any {
                                                it.id != wallet.id && it.name.trim().equals(newName, ignoreCase = true)
                                            }
                                            if (exists) {
                                                Toast.makeText(
                                                    context,
                                                    context.getString(R.string.warn_wallet_exists, newName),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return@TextButton
                                            }
                                            coroutineScope.launch {
                                                val updated = wallet.copy(name = newName, updatedAt = System.currentTimeMillis())
                                                repository.saveWallet(updated)
                                                editingWalletId = null
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.action_simpan),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MizanTheme.colors.accent
                                            )
                                        )
                                    }

                                    TextButton(
                                        onClick = { editingWalletId = null }
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.action_batal),
                                            style = MaterialTheme.typography.bodyMedium.copy(color = MizanTheme.colors.textSecondary)
                                        )
                                    }
                                }
                            } else {
                                // Mode normal: Nama + Badge Mata Uang, Tombol Ubah, Hapus, Pegangan ≡
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Nama dompet & badge mata uang
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = wallet.name,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Medium,
                                                color = MizanTheme.colors.text
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(MizanTheme.colors.surfaceRaised)
                                                .border(0.5.dp, MizanTheme.colors.border, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = wallet.currency.ifBlank { "IDR" },
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MizanTheme.colors.textSecondary
                                                )
                                            )
                                        }
                                    }

                                    // Tombol Ubah
                                    TextButton(
                                        onClick = {
                                            editingWalletId = wallet.id
                                            editingNameText = wallet.name
                                        }
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.action_ubah),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MizanTheme.colors.textSecondary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }

                                    // Tombol Hapus
                                    TextButton(
                                        onClick = {
                                            if (activeWallets.size <= 1) {
                                                Toast.makeText(
                                                    context,
                                                    context.getString(R.string.warn_min_one_wallet),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                walletToDelete = wallet
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.action_hapus),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MizanTheme.colors.expense,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }

                                    // Pegangan geser urutan (≡) dengan ReorderDragHandle
                                    ReorderDragHandle(
                                        isDragging = isDraggingThis,
                                        key = wallet.id,
                                        onDragStart = {
                                            draggingWalletId = wallet.id
                                            dragOffsetY = 0f
                                        },
                                        onDrag = { dragAmountY ->
                                            dragOffsetY += dragAmountY
                                            val currentIndex = activeWalletsList.indexOfFirst { it.id == draggingWalletId }
                                            if (currentIndex != -1) {
                                                if (dragOffsetY > itemHeightThreshold && currentIndex < activeWalletsList.size - 1) {
                                                    val mutable = activeWalletsList.toMutableList()
                                                    val item = mutable.removeAt(currentIndex)
                                                    mutable.add(currentIndex + 1, item)
                                                    activeWalletsList = mutable
                                                    dragOffsetY -= itemHeightThreshold
                                                } else if (dragOffsetY < -itemHeightThreshold && currentIndex > 0) {
                                                    val mutable = activeWalletsList.toMutableList()
                                                    val item = mutable.removeAt(currentIndex)
                                                    mutable.add(currentIndex - 1, item)
                                                    activeWalletsList = mutable
                                                    dragOffsetY += itemHeightThreshold
                                                }
                                            }
                                        },
                                        onDragEnd = {
                                            val draggedId = draggingWalletId
                                            draggingWalletId = null
                                            dragOffsetY = 0f
                                            if (draggedId != null) {
                                                coroutineScope.launch {
                                                    val now = System.currentTimeMillis()
                                                    val updated = activeWalletsList.mapIndexed { index, w ->
                                                        if (w.order != index) {
                                                            w.copy(order = index, updatedAt = now)
                                                        } else {
                                                            w
                                                        }
                                                    }
                                                    repository.saveWallets(updated)
                                                }
                                            }
                                        },
                                        onDragCancel = {
                                            draggingWalletId = null
                                            dragOffsetY = 0f
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // === FORM TAMBAH DOMPET BARU ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.action_tambah_dompet),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Input nama dompet baru
                BasicTextField(
                    value = newWalletName,
                    onValueChange = { newWalletName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 44.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MizanTheme.colors.surfaceRaised)
                        .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MizanTheme.colors.text
                    ),
                    cursorBrush = SolidColor(MizanTheme.colors.accent),
                    singleLine = true,
                    decorationBox = { inner ->
                        if (newWalletName.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.placeholder_nama_dompet),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MizanTheme.colors.textSecondary
                                )
                            )
                        }
                        inner()
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Pilihan mata uang (IDR, JPY, USD)
                val currencyOptions = listOf(
                    DropdownOption("IDR", "IDR"),
                    DropdownOption("JPY", "JPY"),
                    DropdownOption("USD", "USD")
                )
                CustomDropdown(
                    selectedId = newWalletCurrency,
                    options = currencyOptions,
                    placeholder = "IDR",
                    onOptionSelected = { newWalletCurrency = it }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val name = newWalletName.trim()
                        if (name.isEmpty()) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.warn_wallet_empty_name),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        val exists = activeWallets.any { it.name.trim().equals(name, ignoreCase = true) }
                        if (exists) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.warn_wallet_exists, name),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        val maxOrder = if (wallets.isEmpty()) -1 else wallets.maxOf { it.order }
                        val now = System.currentTimeMillis()
                        val newWallet = WalletEntity(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            currency = newWalletCurrency,
                            order = maxOrder + 1,
                            createdAt = now,
                            updatedAt = now,
                            deleted = false
                        )
                        coroutineScope.launch {
                            repository.saveWallet(newWallet)
                            newWalletName = ""
                            newWalletCurrency = "IDR"
                        }
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.colors.accent,
                        contentColor = MizanTheme.colors.onAccent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.action_tambah_dompet),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
