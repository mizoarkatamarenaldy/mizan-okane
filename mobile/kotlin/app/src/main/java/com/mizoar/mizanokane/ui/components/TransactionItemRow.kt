package com.mizoar.mizanokane.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.model.TransactionEntity
import com.mizoar.mizanokane.data.model.WalletEntity
import com.mizoar.mizanokane.domain.formatTimeDisplay
import com.mizoar.mizanokane.ui.theme.MizanTheme

@Composable
fun TransactionItemRow(
    tx: TransactionEntity,
    wallets: List<WalletEntity>,
    is24Hour: Boolean = true,
    onEditClick: (TransactionEntity) -> Unit,
    onDeleteClick: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isTransfer = tx.type == "transfer"
    val isPenyesuaian = tx.type == "penyesuaian"
    val curr = Uang.getTransactionCurrency(tx, wallets)

    val walletMap = wallets.associateBy { it.id }
    val fromWallet = walletMap[tx.walletId ?: DefaultData.DEFAULT_WALLET_ID]
    val fromName = fromWallet?.name ?: DefaultData.DEFAULT_WALLET_NAME
    val fromDeleted = fromWallet?.deleted == true

    val walletText = when {
        isTransfer -> {
            val toWallet = tx.toWalletId?.let { walletMap[it] }
            val toName = toWallet?.name ?: ""
            val toDeleted = toWallet?.deleted == true
            val fromStr = if (fromDeleted) "$fromName (dihapus)" else fromName
            val toStr = if (toDeleted) "$toName (dihapus)" else toName
            "$fromStr → $toStr"
        }
        else -> {
            if (fromDeleted) "$fromName (dihapus)" else fromName
        }
    }

    val (sign, amountColor) = when {
        isTransfer -> "" to MizanTheme.colors.transfer
        isPenyesuaian -> {
            if (tx.amount >= 0) "+" to MizanTheme.colors.accent else "−" to MizanTheme.colors.expense
        }
        tx.type == "income" -> "+" to MizanTheme.colors.accent
        else -> "−" to MizanTheme.colors.expense
    }

    val formattedAmount = "$sign${Uang.formatCurrency(kotlin.math.abs(tx.amount), curr)}"
    val formattedTime = formatTimeDisplay(tx.time, is24Hour)

    val categoryTitle = when {
        isTransfer -> stringResource(id = R.string.label_transfer)
        isPenyesuaian -> {
            val sub = tx.subcategory?.takeIf { it.isNotBlank() } ?: "Koreksi"
            "${stringResource(id = R.string.label_penyesuaian)}: $sub"
        }
        else -> {
            val cat = tx.category ?: ""
            val sub = tx.subcategory?.takeIf { it.isNotBlank() }
            if (sub != null) "$cat > $sub" else cat
        }
    }

    val iconBgColor = when (tx.type) {
        "income" -> MizanTheme.colors.accent.copy(alpha = 0.15f)
        "expense" -> MizanTheme.colors.expense.copy(alpha = 0.15f)
        "transfer" -> MizanTheme.colors.transfer.copy(alpha = 0.15f)
        else -> MizanTheme.colors.surfaceRaised
    }

    val iconTint = when (tx.type) {
        "income" -> MizanTheme.colors.accent
        "expense" -> MizanTheme.colors.expense
        "transfer" -> MizanTheme.colors.transfer
        else -> MizanTheme.colors.textSecondary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = !isPenyesuaian) { onEditClick(tx) }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        val iconDrawableRes = if (isTransfer) R.drawable.ic_transfer else R.drawable.ic_category_default
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconDrawableRes),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Main row: Category + Amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = categoryTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MizanTheme.colors.text
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = formattedAmount,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = amountColor,
                        fontFamily = FontFamily.Default
                    ),
                    maxLines = 1
                )
            }

            // Note row (if any)
            if (!tx.note.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tx.note.trim(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MizanTheme.colors.textSecondary,
                        fontSize = 13.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Sub row: Wallet + Time
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = walletText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MizanTheme.colors.textSecondary,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (formattedTime.isNotBlank()) {
                    Text(
                        text = " • $formattedTime",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MizanTheme.colors.textSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        // Actions: Edit and Delete
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isPenyesuaian) {
                IconButton(
                    onClick = { onEditClick(tx) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = stringResource(id = R.string.title_edit_transaksi),
                        tint = MizanTheme.colors.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            IconButton(
                onClick = { onDeleteClick(tx) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = stringResource(id = R.string.action_hapus),
                    tint = MizanTheme.colors.textSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
