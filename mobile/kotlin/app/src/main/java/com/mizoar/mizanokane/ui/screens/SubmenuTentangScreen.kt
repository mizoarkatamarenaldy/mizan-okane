package com.mizoar.mizanokane.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.ui.theme.MizanTheme

/**
 * Layar Submenu Tentang aplikasi Mizan Okane.
 * Sesuai spek Kotlin 7B dan index.html (#submenu-tentang):
 * - Kartu informasi aplikasi Mizan Okane
 * - Tautan menuju halaman Kebijakan Privasi dan Syarat dan Ketentuan
 * - Tautan Tentang Pengembang yang membuka web portofolio di browser luar
 */
@Composable
fun SubmenuTentangScreen(
    onKebijakanPrivasiClick: () -> Unit,
    onSyaratKetentuanClick: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                text = stringResource(id = R.string.title_tentang),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.text
                )
            )
        }

        // === KONTEN SUBMENU TENTANG ===
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
            ) {
                // Header Kartu Tentang
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(id = R.string.about_app_name),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MizanTheme.colors.text
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.about_app_desc),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            color = MizanTheme.colors.textSecondary
                        )
                    )
                }

                HorizontalDivider(color = MizanTheme.colors.border, thickness = 1.dp)

                // 1. Kebijakan Privasi
                AboutLinkRow(
                    label = stringResource(id = R.string.about_link_privacy),
                    onClick = onKebijakanPrivasiClick
                )

                HorizontalDivider(color = MizanTheme.colors.border, thickness = 1.dp)

                // 2. Syarat dan Ketentuan
                AboutLinkRow(
                    label = stringResource(id = R.string.about_link_terms),
                    onClick = onSyaratKetentuanClick
                )

                HorizontalDivider(color = MizanTheme.colors.border, thickness = 1.dp)

                // 3. Tentang Pengembang
                AboutLinkRow(
                    label = stringResource(id = R.string.about_link_developer),
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://mizoarkatamarenaldy.github.io")
                        )
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
private fun AboutLinkRow(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 52.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MizanTheme.colors.text
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MizanTheme.colors.textSecondary,
            modifier = Modifier.size(18.dp)
        )
    }
}
