package com.mizoar.mizanokane.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.domain.BudgetItemSummary
import com.mizoar.mizanokane.domain.SubcategoryBudgetItemSummary
import com.mizoar.mizanokane.ui.theme.MizanTheme

@Composable
fun BudgetItemView(
    item: BudgetItemSummary,
    modifier: Modifier = Modifier
) {
    val barColor = when {
        item.isOver -> MizanTheme.colors.expense
        item.percent >= 80 -> MizanTheme.colors.budgetWarning
        else -> MizanTheme.colors.accent
    }

    val statusText = if (item.isOver) {
        "Lewat ${Uang.formatCurrency(item.lebihAmount, "IDR")}"
    } else {
        "Sisa ${Uang.formatCurrency(item.sisaAmount, "IDR")}"
    }

    val progressFraction = Uang.hitungProgressFraction(item.terpakaiAmount, item.budgetAmount)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MizanTheme.colors.surfaceRaised),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_category_default),
                        contentDescription = null,
                        tint = MizanTheme.colors.textSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.categoryName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MizanTheme.colors.text
                    )
                )
            }

            Text(
                text = "${Uang.formatCurrency(item.terpakaiAmount, "IDR")} dari ${Uang.formatCurrency(item.budgetAmount, "IDR")}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MizanTheme.colors.textSecondary,
                    fontFamily = FontFamily.Default
                )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Progress bar track (6dp height)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MizanTheme.colors.surfaceRaised)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progressFraction)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(barColor)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Footer: status text & percent
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = barColor,
                    fontSize = 11.sp
                )
            )
            Text(
                text = "${item.percent}%",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = barColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Default
                )
            )
        }

        // Subcategory budgets
        if (item.subcategories.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                for (sub in item.subcategories) {
                    SubcategoryBudgetItemView(sub = sub)
                }
            }
        }
    }
}

@Composable
fun SubcategoryBudgetItemView(
    sub: SubcategoryBudgetItemSummary,
    modifier: Modifier = Modifier
) {
    val barColor = when {
        sub.isOver -> MizanTheme.colors.expense
        sub.percent >= 80 -> MizanTheme.colors.budgetWarning
        else -> MizanTheme.colors.accent
    }

    val statusText = if (sub.isOver) {
        "Lewat ${Uang.formatCurrency(sub.lebihAmount, "IDR")}"
    } else {
        "Sisa ${Uang.formatCurrency(sub.sisaAmount, "IDR")}"
    }

    val progressFraction = Uang.hitungProgressFraction(sub.terpakaiAmount, sub.budgetAmount)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sub.subcategoryName,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MizanTheme.colors.textSecondary,
                    fontSize = 12.sp
                )
            )
            Text(
                text = "${Uang.formatCurrency(sub.terpakaiAmount, "IDR")} dari ${Uang.formatCurrency(sub.budgetAmount, "IDR")}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MizanTheme.colors.textSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Default
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MizanTheme.colors.surfaceRaised)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progressFraction)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = barColor,
                    fontSize = 10.sp
                )
            )
            Text(
                text = "${sub.percent}%",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = barColor,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Default
                )
            )
        }
    }
}
