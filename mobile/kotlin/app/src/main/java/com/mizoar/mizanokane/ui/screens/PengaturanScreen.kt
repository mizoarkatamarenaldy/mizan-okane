package com.mizoar.mizanokane.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.ui.theme.MizanTheme
import com.mizoar.mizanokane.ui.theme.danger

/**
 * Layar Pengaturan aplikasi Mizan Okane.
 * Sesuai spek Kotlin 7A dan 7B:
 * Menu utama Pengaturan dengan urutan dan teks judul serta deskripsi sama persis dengan web:
 * 1. Tampilan: "Tema dan format jam"
 * 2. Kategori: "Pengeluaran, pemasukan, dan sub kategori"
 * 3. Dompet: "Kelola dompet dan rekening sumber dana"
 * 4. Budget: "Batas pengeluaran bulanan per kategori"
 * 5. Data: "Ekspor CSV dan JSON, impor JSON, data contoh"
 * 6. Akun dan Sinkron: "Google Drive dan status sinkronisasi"
 * 7. Tentang: "Kebijakan privasi, syarat ketentuan, dan pengembang"
 * 8. Zona Bahaya (grup terpisah di paling bawah): "Hapus seluruh data transaksi"
 */
@Composable
fun PengaturanScreen(
    onTampilanClick: () -> Unit,
    onKelolaKategoriClick: () -> Unit,
    onKelolaDompetClick: () -> Unit,
    onKelolaBudgetClick: () -> Unit,
    onDataClick: () -> Unit,
    onAkunSinkronClick: () -> Unit,
    onTentangClick: () -> Unit,
    onZonaBahayaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MizanTheme.colors.bg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.nav_pengaturan),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MizanTheme.colors.text
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Grup 1: Menu Pengaturan Utama
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MizanTheme.colors.surface)
                .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
        ) {
            // 1. Tampilan
            SettingsMenuRow(
                iconRes = R.drawable.ic_sun,
                title = stringResource(id = R.string.title_tampilan),
                desc = stringResource(id = R.string.desc_tampilan),
                onClick = onTampilanClick
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MizanTheme.colors.border)
            )

            // 2. Kategori
            SettingsMenuRow(
                iconRes = R.drawable.ic_category_default,
                title = stringResource(id = R.string.title_kategori),
                desc = stringResource(id = R.string.desc_kelola_kategori),
                onClick = onKelolaKategoriClick
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MizanTheme.colors.border)
            )

            // 3. Dompet
            SettingsMenuRow(
                iconRes = R.drawable.ic_wallet,
                title = stringResource(id = R.string.title_dompet),
                desc = stringResource(id = R.string.desc_kelola_dompet),
                onClick = onKelolaDompetClick
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MizanTheme.colors.border)
            )

            // 4. Budget
            SettingsMenuRow(
                iconRes = R.drawable.ic_budget,
                title = stringResource(id = R.string.title_kelola_budget),
                desc = stringResource(id = R.string.desc_kelola_budget),
                onClick = onKelolaBudgetClick
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MizanTheme.colors.border)
            )

            // 5. Data
            SettingsMenuRow(
                iconRes = R.drawable.ic_data,
                title = stringResource(id = R.string.title_data),
                desc = stringResource(id = R.string.desc_data),
                onClick = onDataClick
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MizanTheme.colors.border)
            )

            // 6. Akun dan Sinkron
            SettingsMenuRow(
                iconRes = R.drawable.ic_cloud,
                title = stringResource(id = R.string.title_akun_sinkron),
                desc = stringResource(id = R.string.desc_akun_sinkron),
                onClick = onAkunSinkronClick
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MizanTheme.colors.border)
            )

            // 7. Tentang
            SettingsMenuRow(
                iconRes = R.drawable.ic_info,
                title = stringResource(id = R.string.title_tentang),
                desc = stringResource(id = R.string.desc_tentang),
                onClick = onTentangClick
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Grup 2: Zona Bahaya (terpisah di paling bawah)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MizanTheme.colors.surface)
                .border(1.dp, MizanTheme.colors.danger.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
        ) {
            SettingsMenuRow(
                iconRes = R.drawable.ic_danger,
                title = stringResource(id = R.string.title_zona_bahaya),
                desc = stringResource(id = R.string.desc_zona_bahaya),
                iconTint = MizanTheme.colors.danger,
                titleColor = MizanTheme.colors.danger,
                onClick = onZonaBahayaClick
            )
        }
    }
}

@Composable
private fun SettingsMenuRow(
    iconRes: Int,
    title: String,
    desc: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = MizanTheme.colors.textSecondary,
    titleColor: Color = MizanTheme.colors.text
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = MizanTheme.colors.textSecondary
                )
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = MizanTheme.colors.textSecondary,
            modifier = Modifier.size(20.dp)
        )
    }
}
