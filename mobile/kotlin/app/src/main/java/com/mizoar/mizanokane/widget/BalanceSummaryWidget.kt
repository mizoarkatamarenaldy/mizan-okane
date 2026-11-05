package com.mizoar.mizanokane.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.defaultWeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.mizoar.mizanokane.MainActivity
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.data.json.JsonBackupManager
import com.mizoar.mizanokane.data.json.TransactionDto
import com.mizoar.mizanokane.data.json.WalletDto
import com.mizoar.mizanokane.data.local.AppDatabase
import com.mizoar.mizanokane.domain.formatMonthYear
import com.mizoar.mizanokane.domain.getActiveCurrencies
import com.mizoar.mizanokane.domain.getBerandaMonthSummary
import com.mizoar.mizanokane.domain.getCurrentYearMonthString
import com.mizoar.mizanokane.ui.theme.ColorExpense
import com.mizoar.mizanokane.ui.theme.DarkAccent
import com.mizoar.mizanokane.ui.theme.DarkText
import com.mizoar.mizanokane.ui.theme.DarkTextSecondary
import com.mizoar.mizanokane.ui.theme.LightAccent
import com.mizoar.mizanokane.ui.theme.LightText
import com.mizoar.mizanokane.ui.theme.LightTextSecondary
import kotlinx.serialization.builtins.ListSerializer

private val TextColor = ColorProvider(day = LightText, night = DarkText)
private val TextSecondaryColor = ColorProvider(day = LightTextSecondary, night = DarkTextSecondary)
private val IncomeColor = ColorProvider(day = LightAccent, night = DarkAccent)
private val ExpenseColor = ColorProvider(day = ColorExpense, night = ColorExpense)

class BalanceSummaryWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val db = AppDatabase.getInstance(context)

        val isDemo = try {
            db.metaDao().get("demo_mode") == "true"
        } catch (_: Exception) {
            false
        }

        val transactions = if (isDemo) {
            val jsonStr = try { db.metaDao().get("demo_transactions") } catch (_: Exception) { null }
            if (jsonStr.isNullOrBlank()) {
                emptyList()
            } else {
                try {
                    JsonBackupManager.json.decodeFromString(
                        ListSerializer(TransactionDto.serializer()),
                        jsonStr
                    ).map { it.toEntity() }
                } catch (_: Exception) {
                    emptyList()
                }
            }
        } else {
            try {
                db.transactionDao().getAll()
            } catch (_: Exception) {
                emptyList()
            }
        }

        val rawWallets = if (isDemo) {
            val jsonStr = try { db.metaDao().get("demo_wallets") } catch (_: Exception) { null }
            if (jsonStr.isNullOrBlank()) {
                DefaultData.DEFAULT_WALLETS
            } else {
                try {
                    JsonBackupManager.json.decodeFromString(
                        ListSerializer(WalletDto.serializer()),
                        jsonStr
                    ).map { it.toEntity() }.sortedBy { it.order }
                } catch (_: Exception) {
                    DefaultData.DEFAULT_WALLETS
                }
            }
        } else {
            try {
                db.walletDao().getAll()
            } catch (_: Exception) {
                emptyList()
            }
        }
        val wallets = if (rawWallets.isEmpty()) DefaultData.DEFAULT_WALLETS else rawWallets

        val currentMonthKey = getCurrentYearMonthString()
        val monthLabel = formatMonthYear(currentMonthKey)
        val activeCurrencies = getActiveCurrencies(wallets)

        val berandaSummary = getBerandaMonthSummary(
            transactions = transactions,
            targetMonthKey = currentMonthKey,
            activeCurrencies = activeCurrencies,
            wallets = wallets
        )

        val incomeStr = activeCurrencies.joinToString("  ") { c ->
            val inc = berandaSummary.incomeByCurrency[c] ?: 0L
            "+${Uang.formatCurrency(inc, c)}"
        }

        val expenseStr = activeCurrencies.joinToString("  ") { c ->
            val exp = berandaSummary.expenseByCurrency[c] ?: 0L
            "−${Uang.formatCurrency(exp, c)}"
        }

        val balanceStr = activeCurrencies.joinToString("  ") { c ->
            val bal = berandaSummary.totalBalanceByCurrency[c] ?: 0L
            Uang.formatCurrency(bal, c)
        }

        val incomeLabel = context.getString(R.string.label_pemasukan)
        val expenseLabel = context.getString(R.string.label_pengeluaran)
        val saldoLabel = context.getString(R.string.label_saldo)

        provideContent {
            BalanceSummaryWidgetContent(
                monthLabel = monthLabel,
                incomeLabel = incomeLabel,
                incomeText = incomeStr,
                expenseLabel = expenseLabel,
                expenseText = expenseStr,
                saldoLabel = saldoLabel,
                balanceText = balanceStr
            )
        }
    }
}

@Composable
fun BalanceSummaryWidgetContent(
    monthLabel: String,
    incomeLabel: String,
    incomeText: String,
    expenseLabel: String,
    expenseText: String,
    saldoLabel: String,
    balanceText: String
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .background(R.drawable.bg_widget)
            .cornerRadius(8.dp)
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        // Nama bulan berjalan tampil kecil di bagian atas widget
        Text(
            text = monthLabel,
            style = TextStyle(
                color = TextSecondaryColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = GlanceModifier.defaultWeight())

        // Baris 1: Pemasukan
        SummaryRow(
            label = incomeLabel,
            value = incomeText,
            valueColor = IncomeColor,
            valueWeight = FontWeight.Medium
        )

        Spacer(modifier = GlanceModifier.height(6.dp))

        // Baris 2: Pengeluaran
        SummaryRow(
            label = expenseLabel,
            value = expenseText,
            valueColor = ExpenseColor,
            valueWeight = FontWeight.Medium
        )

        Spacer(modifier = GlanceModifier.height(6.dp))

        // Baris 3: Saldo
        SummaryRow(
            label = saldoLabel,
            value = balanceText,
            valueColor = TextColor,
            valueWeight = FontWeight.Bold
        )

        Spacer(modifier = GlanceModifier.defaultWeight())
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: ColorProvider,
    valueWeight: FontWeight
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        Text(
            text = label,
            modifier = GlanceModifier.defaultWeight(),
            style = TextStyle(
                color = TextSecondaryColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
        Text(
            text = value,
            style = TextStyle(
                color = valueColor,
                fontSize = 14.sp,
                fontWeight = valueWeight
            )
        )
    }
}
