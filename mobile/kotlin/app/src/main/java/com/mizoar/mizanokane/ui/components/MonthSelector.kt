package com.mizoar.mizanokane.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.ui.theme.MizanTheme

@Composable
fun MonthSelector(
    monthLabel: String,
    isNextEnabled: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Bulan sebelumnya",
                tint = MizanTheme.colors.textSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = monthLabel,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MizanTheme.colors.text
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        IconButton(
            onClick = onNextClick,
            enabled = isNextEnabled,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Bulan sesudahnya",
                tint = MizanTheme.colors.textSecondary,
                modifier = Modifier
                    .size(20.dp)
                    .alpha(if (isNextEnabled) 1.0f else 0.3f)
            )
        }
    }
}
