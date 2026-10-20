package com.mizoar.mizanokane.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.WalletEntity
import com.mizoar.mizanokane.ui.theme.MizanTheme

@Composable
fun WalletBalanceItem(
    wallet: WalletEntity,
    balanceCents: Long,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isZero = balanceCents == 0L
    val borderColor = if (isSelected) MizanTheme.colors.accent else MizanTheme.colors.border
    val formattedBalance = Uang.formatCurrency(balanceCents, wallet.currency.ifBlank { "IDR" })

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 100.dp, minHeight = 44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MizanTheme.colors.surface)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .alpha(if (isZero && !isSelected) 0.6f else 1.0f)
    ) {
        Column {
            Text(
                text = wallet.name,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp,
                    color = MizanTheme.colors.textSecondary
                )
            )
            Text(
                text = formattedBalance,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.text,
                    fontFamily = FontFamily.Default
                )
            )
        }
    }
}
