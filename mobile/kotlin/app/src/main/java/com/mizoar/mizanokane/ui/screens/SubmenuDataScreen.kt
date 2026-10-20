package com.mizoar.mizanokane.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.json.JsonBackupManager
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.domain.CsvExporter
import com.mizoar.mizanokane.ui.theme.MizanTheme
import java.time.LocalDate
import kotlinx.coroutines.launch

/**
 * Layar Submenu Data aplikasi Mizan Okane.
 * Sesuai spek Kotlin 7B dan index.html (#submenu-data):
 * - Cadangan dan Pemulihan: Ekspor JSON, Ekspor CSV, Impor JSON
 * - Data Contoh: Isi Data Contoh, Keluar Mode Data Contoh
 * Menggunakan Storage Access Framework (SAF) untuk ekspor dan impor berkas.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubmenuDataScreen(
    repository: MizanRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isDemo by repository.isDemoModeFlow().collectAsState(initial = false)

    var showFillConfirmDialog by remember { mutableStateOf(false) }
    var showExitConfirmDialog by remember { mutableStateOf(false) }

    // SAF Activity Result Launchers
    val exportJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                try {
                    val jsonContent = JsonBackupManager.exportToJson(repository)
                    context.contentResolver.openOutputStream(uri)?.use { stream ->
                        stream.write(jsonContent.toByteArray(Charsets.UTF_8))
                    }
                    Toast.makeText(context, context.getString(R.string.msg_export_json_success), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, context.getString(R.string.msg_export_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                try {
                    val txs = repository.getRealTransactions()
                    val wallets = repository.getRealWallets()
                    val csvContent = CsvExporter.generateCsv(txs, wallets)
                    context.contentResolver.openOutputStream(uri)?.use { stream ->
                        stream.write(csvContent.toByteArray(Charsets.UTF_8))
                    }
                    Toast.makeText(context, context.getString(R.string.msg_export_csv_success), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, context.getString(R.string.msg_export_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val importJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                try {
                    val content = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                    if (content.isNullOrBlank()) {
                        Toast.makeText(context, context.getString(R.string.msg_import_empty_or_invalid), Toast.LENGTH_LONG).show()
                        return@launch
                    }
                    val result = JsonBackupManager.importFromJson(content, repository)
                    if (result.success) {
                        val details = mutableListOf<String>()
                        if (result.transactionsAdded > 0) details.add("${result.transactionsAdded} transaksi baru ditambahkan")
                        if (result.transactionsUpdated > 0) details.add("${result.transactionsUpdated} transaksi diperbarui")
                        if (result.walletsChanged) details.add("daftar dompet diperbarui")
                        if (result.categoriesChanged) details.add("kategori diperbarui")
                        if (result.budgetsChanged) details.add("budget diperbarui")
                        if (result.settingsChanged) details.add("pengaturan diperbarui")

                        val detailStr = if (details.isNotEmpty()) details.joinToString(", ") else "data diperbarui"
                        val msg = if (details.isNotEmpty()) {
                            "Impor data berhasil ($detailStr)."
                        } else {
                            context.getString(R.string.msg_import_no_changes)
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, result.message.ifBlank { context.getString(R.string.msg_import_empty_or_invalid) }, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, context.getString(R.string.msg_import_empty_or_invalid), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Dialog Konfirmasi Isi / Muat Ulang Data Contoh
    if (showFillConfirmDialog) {
        val titleText = if (isDemo) {
            stringResource(id = R.string.dialog_reload_dummy_title)
        } else {
            stringResource(id = R.string.dialog_activate_dummy_title)
        }
        val confirmBtnText = if (isDemo) {
            stringResource(id = R.string.action_muat_ulang)
        } else {
            stringResource(id = R.string.action_aktifkan)
        }

        AlertDialog(
            onDismissRequest = { showFillConfirmDialog = false },
            containerColor = MizanTheme.colors.surface,
            shape = RoundedCornerShape(8.dp),
            title = {
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showFillConfirmDialog = false
                        coroutineScope.launch {
                            repository.activateSampleData(LocalDate.now())
                            Toast.makeText(context, context.getString(R.string.msg_dummy_active), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.colors.accent,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = confirmBtnText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFillConfirmDialog = false }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        color = MizanTheme.colors.textSecondary
                    )
                }
            }
        )
    }

    // Dialog Konfirmasi Keluar Mode Data Contoh
    if (showExitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showExitConfirmDialog = false },
            containerColor = MizanTheme.colors.surface,
            shape = RoundedCornerShape(8.dp),
            title = {
                Text(
                    text = stringResource(id = R.string.dialog_exit_dummy_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitConfirmDialog = false
                        coroutineScope.launch {
                            repository.clearSampleData()
                            Toast.makeText(context, context.getString(R.string.msg_dummy_exit_success), Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.colors.accent,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = stringResource(id = R.string.action_keluar))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmDialog = false }) {
                    Text(
                        text = stringResource(id = R.string.action_batal),
                        color = MizanTheme.colors.textSecondary
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
                    contentDescription = stringResource(id = R.string.desc_kembali_ke_pengaturan),
                    tint = MizanTheme.colors.text
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(id = R.string.title_data),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.text
                )
            )
        }

        // === KONTEN SUBMENU DATA ===
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                // Bagian 1: Cadangan dan Pemulihan
                Text(
                    text = stringResource(id = R.string.title_cadangan_pemulihan),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.desc_cadangan_pemulihan),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        color = MizanTheme.colors.textSecondary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DataActionButton(
                        text = stringResource(id = R.string.action_ekspor_json),
                        onClick = {
                            val localDate = LocalDate.now().toString()
                            exportJsonLauncher.launch("mizan-okane-backup-$localDate.json")
                        }
                    )

                    DataActionButton(
                        text = stringResource(id = R.string.action_ekspor_csv),
                        onClick = {
                            coroutineScope.launch {
                                val txs = repository.getRealTransactions().filter { !it.deleted && !it.dummy }
                                if (txs.isEmpty()) {
                                    Toast.makeText(context, context.getString(R.string.msg_csv_empty), Toast.LENGTH_SHORT).show()
                                } else {
                                    val localDate = LocalDate.now().toString()
                                    exportCsvLauncher.launch("mizan-okane-transaksi-$localDate.csv")
                                }
                            }
                        }
                    )

                    DataActionButton(
                        text = stringResource(id = R.string.action_impor_json),
                        onClick = {
                            if (isDemo) {
                                Toast.makeText(context, context.getString(R.string.msg_cannot_import_in_dummy_mode), Toast.LENGTH_LONG).show()
                            } else {
                                importJsonLauncher.launch(arrayOf("application/json", "text/*"))
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = MizanTheme.colors.border, thickness = 1.dp)
                Spacer(modifier = Modifier.height(18.dp))

                // Bagian 2: Data Contoh
                Text(
                    text = stringResource(id = R.string.title_data_contoh),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.desc_data_contoh),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        color = MizanTheme.colors.textSecondary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DataActionButton(
                        text = stringResource(id = R.string.action_isi_data_contoh),
                        onClick = { showFillConfirmDialog = true }
                    )

                    DataActionButton(
                        text = stringResource(id = R.string.action_keluar_mode_data_contoh),
                        enabled = isDemo,
                        onClick = {
                            if (isDemo) {
                                showExitConfirmDialog = true
                            } else {
                                Toast.makeText(context, context.getString(R.string.msg_not_in_dummy_mode), Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DataActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MizanTheme.colors.surfaceRaised,
            contentColor = MizanTheme.colors.text,
            disabledContainerColor = MizanTheme.colors.surfaceRaised.copy(alpha = 0.5f),
            disabledContentColor = MizanTheme.colors.textSecondary.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(
            1.dp,
            if (enabled) MizanTheme.colors.border else MizanTheme.colors.border.copy(alpha = 0.4f)
        ),
        modifier = modifier.defaultMinSize(minHeight = 44.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
