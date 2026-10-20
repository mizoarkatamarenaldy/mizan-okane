package com.mizoar.mizanokane.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.ui.theme.MizanTheme
import com.mizoar.mizanokane.ui.theme.danger
import kotlinx.coroutines.launch

/**
 * Layar Submenu Zona Bahaya aplikasi Mizan Okane.
 * Sesuai spek Kotlin 7A:
 * - Kartu Zona Bahaya dengan tombol "Hapus Semua Data"
 * - Dialog konfirmasi Compose (dua langkah): input kata "HAPUS"
 * - Reset setelan pabrik: menghapus transaksi, mengembalikan dompet, kategori, budget, dan pengaturan ke awal
 *   dalam satu transaksi atomik repository.resetToDefaults().
 */
@Composable
fun SubmenuBahayaScreen(
    repository: MizanRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showConfirmDialog by remember { mutableStateOf(false) }
    var confirmInputText by remember { mutableStateOf("") }
    var isResetting by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        val isMatch = confirmInputText.trim() == "HAPUS"

        AlertDialog(
            onDismissRequest = {
                if (!isResetting) {
                    showConfirmDialog = false
                    confirmInputText = ""
                }
            },
            containerColor = MizanTheme.colors.surface,
            shape = RoundedCornerShape(8.dp),
            title = {
                Text(
                    text = stringResource(id = R.string.dialog_delete_all_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MizanTheme.colors.text
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = stringResource(id = R.string.dialog_delete_all_desc),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.dialog_delete_all_prompt),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MizanTheme.colors.text,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = confirmInputText,
                        onValueChange = { confirmInputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MizanTheme.colors.surfaceRaised)
                            .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MizanTheme.colors.text,
                            fontSize = 14.sp
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MizanTheme.colors.accent),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (confirmInputText.isEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.dialog_delete_all_placeholder),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MizanTheme.colors.textSecondary.copy(alpha = 0.6f),
                                            fontSize = 14.sp
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isMatch && !isResetting) {
                            isResetting = true
                            coroutineScope.launch {
                                try {
                                    repository.resetToDefaults()
                                    showConfirmDialog = false
                                    confirmInputText = ""
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.toast_delete_all_success),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onNavigateBack()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.toast_delete_all_error, e.message ?: ""),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } finally {
                                    isResetting = false
                                }
                            }
                        }
                    },
                    enabled = isMatch && !isResetting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.colors.danger,
                        contentColor = Color.White,
                        disabledContainerColor = MizanTheme.colors.surfaceRaised,
                        disabledContentColor = MizanTheme.colors.textSecondary
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.action_hapus_semua_data),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (!isResetting) {
                            showConfirmDialog = false
                            confirmInputText = ""
                        }
                    },
                    enabled = !isResetting
                ) {
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
                    contentDescription = stringResource(id = R.string.desc_kembali_ke_pengaturan),
                    tint = MizanTheme.colors.text
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(id = R.string.title_zona_bahaya),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.text
                )
            )
        }

        // === KONTEN (SCROLLABLE) ===
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
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
                Text(
                    text = stringResource(id = R.string.title_zona_bahaya),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.desc_zona_bahaya_card),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = MizanTheme.colors.textSecondary,
                        lineHeight = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showConfirmDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.colors.danger,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.action_hapus_semua_data),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}
