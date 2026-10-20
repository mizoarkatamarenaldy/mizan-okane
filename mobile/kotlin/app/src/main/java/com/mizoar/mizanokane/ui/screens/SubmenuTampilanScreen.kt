package com.mizoar.mizanokane.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.data.DefaultData
import com.mizoar.mizanokane.data.repository.MizanRepository
import com.mizoar.mizanokane.ui.theme.MizanTheme
import kotlinx.coroutines.launch

/**
 * Layar Submenu Tampilan aplikasi Mizan Okane.
 * Sesuai spek Kotlin 7A:
 * - Opsi Tema: "Ikuti sistem" (system), "Terang" (light), "Gelap" (dark)
 * - Opsi Format Jam: "24 jam (contoh: 14:30)" (24), "12 jam (contoh: 02:30 PM)" (12)
 * Disimpan lewat settings yang sudah ada dan langsung berlaku ke seluruh aplikasi.
 */
@Composable
fun SubmenuTampilanScreen(
    repository: MizanRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val settings by repository.getSettingsFlow().collectAsState(initial = DefaultData.DEFAULT_SETTINGS)

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
                text = stringResource(id = R.string.title_tampilan),
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
            // Kartu 1: Tema
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title_tema),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.desc_tema),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = MizanTheme.colors.textSecondary,
                        lineHeight = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))

                val themeOptions = listOf(
                    "system" to stringResource(id = R.string.theme_system),
                    "light" to stringResource(id = R.string.theme_light),
                    "dark" to stringResource(id = R.string.theme_dark)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    themeOptions.forEach { (key, label) ->
                        val isSelected = settings.theme == key
                        RadioOptionRow(
                            label = label,
                            isSelected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    coroutineScope.launch {
                                        repository.saveSettings(
                                            settings.copy(
                                                theme = key,
                                                updatedAt = System.currentTimeMillis()
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Kartu 2: Format Jam
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title_format_jam),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MizanTheme.colors.text
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.desc_format_jam),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = MizanTheme.colors.textSecondary,
                        lineHeight = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))

                val timeFormatOptions = listOf(
                    "24" to stringResource(id = R.string.time_format_24),
                    "12" to stringResource(id = R.string.time_format_12)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    timeFormatOptions.forEach { (key, label) ->
                        val isSelected = settings.timeFormat == key
                        RadioOptionRow(
                            label = label,
                            isSelected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    coroutineScope.launch {
                                        repository.saveSettings(
                                            settings.copy(
                                                timeFormat = key,
                                                updatedAt = System.currentTimeMillis()
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RadioOptionRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MizanTheme.colors.accent else MizanTheme.colors.border
    val bgColor = if (isSelected) MizanTheme.colors.accent.copy(alpha = 0.12f) else MizanTheme.colors.surfaceRaised

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 44.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MizanTheme.colors.accent,
                unselectedColor = MizanTheme.colors.textSecondary
            ),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.5.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = MizanTheme.colors.text
            )
        )
    }
}
