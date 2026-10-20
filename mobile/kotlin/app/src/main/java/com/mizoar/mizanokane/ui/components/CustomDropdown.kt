package com.mizoar.mizanokane.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.ui.theme.MizanTheme

data class DropdownOption(
    val id: String,
    val label: String,
    @DrawableRes val iconRes: Int? = null
)

@Composable
fun CustomDropdown(
    selectedId: String?,
    options: List<DropdownOption>,
    placeholder: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = options.find { it.id == selectedId }
    val displayText = selectedOption?.label ?: placeholder
    val isPlaceholder = selectedOption == null

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 44.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MizanTheme.colors.surface)
                .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(6.dp))
                .clickable { expanded = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedOption?.iconRes != null) {
                    Icon(
                        painter = painterResource(id = selectedOption.iconRes),
                        contentDescription = null,
                        tint = MizanTheme.colors.text,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = if (isPlaceholder) MizanTheme.colors.textSecondary else MizanTheme.colors.text
                    )
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = null,
                tint = MizanTheme.colors.textSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MizanTheme.colors.surface)
                .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
        ) {
            for (option in options) {
                val isSelected = option.id == selectedId
                DropdownMenuItem(
                    leadingIcon = option.iconRes?.let { resId ->
                        {
                            Icon(
                                painter = painterResource(id = resId),
                                contentDescription = null,
                                tint = if (isSelected) MizanTheme.colors.accent else MizanTheme.colors.text,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    text = {
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) MizanTheme.colors.accent else MizanTheme.colors.text
                            )
                        )
                    },
                    onClick = {
                        onOptionSelected(option.id)
                        expanded = false
                    },
                    modifier = Modifier
                        .defaultMinSize(minHeight = 44.dp)
                        .background(
                            if (isSelected) MizanTheme.colors.surfaceRaised else MizanTheme.colors.surface
                        )
                )
            }
        }
    }
}
