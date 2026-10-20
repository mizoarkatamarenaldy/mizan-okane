package com.mizoar.mizanokane.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.WalletEntity
import com.mizoar.mizanokane.ui.theme.MizanTheme

private val POSITIVE_CATEGORIES = listOf("Bunga", "Cashback", "Koreksi")
private val NEGATIVE_CATEGORIES = listOf("Biaya admin", "Lupa catat", "Koreksi")

@Composable
fun AdjustBalanceDialog(
    wallet: WalletEntity,
    appBalance: Long,
    onDismissRequest: () -> Unit,
    onConfirm: (subcat: String, diffCents: Long) -> Unit
) {
    val currency = wallet.currency.takeIf { it.isNotBlank() } ?: "IDR"
    var realBalanceText by remember { mutableStateOf("") }

    val hasInput = realBalanceText.isNotBlank()
    val parsedRealBalance = remember(realBalanceText, currency) {
        if (hasInput) Uang.parseAmountInput(realBalanceText, currency) else 0L
    }

    val diff = if (hasInput) parsedRealBalance - appBalance else 0L
    val isSame = hasInput && diff == 0L
    val isPositive = diff > 0L
    val isNegative = diff < 0L

    var selectedCategory by remember { mutableStateOf("Bunga") }

    LaunchedEffect(isPositive, isNegative) {
        if (isPositive && selectedCategory !in POSITIVE_CATEGORIES) {
            selectedCategory = "Bunga"
        } else if (isNegative && selectedCategory !in NEGATIVE_CATEGORIES) {
            selectedCategory = "Koreksi"
        }
    }

    val currencyPrefix = when (currency) {
        "USD" -> "$"
        "JPY" -> "¥"
        else -> "Rp"
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MizanTheme.colors.surface)
                .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Judul dialog
                Text(
                    text = stringResource(id = R.string.title_dialog_sesuaikan_saldo),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MizanTheme.colors.text
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Pembanding saldo di app
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MizanTheme.colors.surfaceRaised)
                        .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = wallet.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MizanTheme.colors.textSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.label_saldo_di_app),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MizanTheme.colors.text
                                )
                            )
                            Text(
                                text = Uang.formatCurrency(appBalance, currency),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MizanTheme.colors.text
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Kolom input saldo riil
                Text(
                    text = stringResource(id = R.string.label_saldo_riil_sekarang, currencyPrefix),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MizanTheme.colors.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                AmountInputField(
                    amountText = realBalanceText,
                    onAmountChange = { realBalanceText = it },
                    currency = currency
                )

                // Selisih sebelum disimpan / pesan saldo sudah sama
                if (hasInput) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(MizanTheme.colors.surfaceRaised)
                            .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        if (isSame) {
                            Text(
                                text = stringResource(id = R.string.msg_saldo_sudah_sama),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MizanTheme.colors.textSecondary,
                                    fontWeight = FontWeight.Medium
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.label_selisih),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MizanTheme.colors.textSecondary
                                    )
                                )
                                val diffText = if (isPositive) {
                                    "+" + Uang.formatCurrency(diff, currency)
                                } else {
                                    "−" + Uang.formatCurrency(kotlin.math.abs(diff), currency)
                                }
                                val diffColor = if (isPositive) MizanTheme.colors.accent else MizanTheme.colors.expense
                                Text(
                                    text = diffText,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = diffColor
                                    )
                                )
                            }
                        }
                    }
                }

                // Pilihan kategori penyesuaian
                if (hasInput && !isSame) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(id = R.string.label_kategori_penyesuaian),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val categories = if (isPositive) POSITIVE_CATEGORIES else NEGATIVE_CATEGORIES
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (cat in categories) {
                            val isActive = (cat == selectedCategory)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(minHeight = 44.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isActive) MizanTheme.colors.accent else MizanTheme.colors.surfaceRaised)
                                    .border(
                                        1.dp,
                                        if (isActive) MizanTheme.colors.accent else MizanTheme.colors.border,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 6.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                                        color = if (isActive) MizanTheme.colors.onAccent else MizanTheme.colors.textSecondary
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol aksi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.action_batal),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MizanTheme.colors.textSecondary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (hasInput && !isSame) {
                                onConfirm(selectedCategory, diff)
                            }
                        },
                        enabled = hasInput && !isSame,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MizanTheme.colors.accent,
                            contentColor = MizanTheme.colors.onAccent,
                            disabledContainerColor = MizanTheme.colors.surfaceRaised,
                            disabledContentColor = MizanTheme.colors.textSecondary
                        ),
                        modifier = Modifier.defaultMinSize(minHeight = 44.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.action_konfirmasi),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}
