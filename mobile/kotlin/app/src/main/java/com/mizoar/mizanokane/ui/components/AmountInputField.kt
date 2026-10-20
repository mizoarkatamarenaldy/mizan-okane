package com.mizoar.mizanokane.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.data.Uang
import com.mizoar.mizanokane.ui.theme.MizanTheme

@Composable
fun AmountInputField(
    amountText: String,
    onAmountChange: (String) -> Unit,
    currency: String,
    modifier: Modifier = Modifier,
    placeholder: String = "0"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(MizanTheme.colors.surface)
            .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (currency) {
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
                if (currency == "USD") {
                    val clean = input.filter { it.isDigit() || it == ',' || it == '.' }
                    val sepIdx = clean.indexOfFirst { it == ',' || it == '.' }
                    if (sepIdx != -1) {
                        val intP = clean.substring(0, sepIdx).filter { it.isDigit() }
                        val decP = clean.substring(sepIdx + 1).filter { it.isDigit() }.take(2)
                        val intFormatted = if (intP.isNotEmpty()) Uang.formatNumber(intP.toLongOrNull() ?: 0L) else "0"
                        onAmountChange("$intFormatted,$decP")
                    } else {
                        val digits = clean.filter { it.isDigit() }
                        onAmountChange(if (digits.isNotEmpty()) Uang.formatNumber(digits.toLongOrNull() ?: 0L) else "")
                    }
                } else {
                    val digits = input.filter { it.isDigit() }
                    onAmountChange(
                        if (digits.isNotEmpty()) {
                            Uang.formatNumber(digits.toLongOrNull() ?: 0L)
                        } else {
                            ""
                        }
                    )
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
                keyboardType = if (currency == "USD") KeyboardType.Decimal else KeyboardType.Number
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (amountText.isEmpty()) {
                    Text(
                        text = placeholder,
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
}
