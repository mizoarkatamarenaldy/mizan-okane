package com.mizoar.mizanokane.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mizoar.mizanokane.R
import com.mizoar.mizanokane.ui.theme.MizanTheme

/**
 * Layar Kebijakan Privasi aplikasi Mizan Okane.
 * Sesuai dengan desktop/src/privasi.html.
 */
@Composable
fun KebijakanPrivasiScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MizanTheme.colors.bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        // === HEADER ATAS ===
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
                text = stringResource(id = R.string.privacy_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MizanTheme.colors.text
                )
            )
        }

        // === KONTEN TEKS ===
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MizanTheme.colors.surface)
                    .border(1.dp, MizanTheme.colors.border, RoundedCornerShape(8.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.privacy_title),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MizanTheme.colors.text
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.privacy_updated),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = MizanTheme.colors.textSecondary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MizanTheme.colors.border, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Bagian 1
                PrivacySectionHeader(title = stringResource(id = R.string.privacy_sec1_title))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec1_intro))
                Spacer(modifier = Modifier.height(8.dp))
                PrivacyBulletPoint(text = stringResource(id = R.string.privacy_sec1_p1))
                Spacer(modifier = Modifier.height(6.dp))
                PrivacyBulletPoint(text = stringResource(id = R.string.privacy_sec1_p2))

                Spacer(modifier = Modifier.height(18.dp))

                // Bagian 2
                PrivacySectionHeader(title = stringResource(id = R.string.privacy_sec2_title))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec2_p1))
                Spacer(modifier = Modifier.height(8.dp))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec2_p2))

                Spacer(modifier = Modifier.height(18.dp))

                // Bagian 3
                PrivacySectionHeader(title = stringResource(id = R.string.privacy_sec3_title))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec3_p1))
                Spacer(modifier = Modifier.height(8.dp))
                PrivacyCodeBlock(text = stringResource(id = R.string.privacy_sec3_p2))
                Spacer(modifier = Modifier.height(8.dp))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec3_p3))
                Spacer(modifier = Modifier.height(8.dp))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec3_p4))

                Spacer(modifier = Modifier.height(18.dp))

                // Bagian 4
                PrivacySectionHeader(title = stringResource(id = R.string.privacy_sec4_title))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec4_p1))

                Spacer(modifier = Modifier.height(18.dp))

                // Bagian 5
                PrivacySectionHeader(title = stringResource(id = R.string.privacy_sec5_title))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec5_intro))
                Spacer(modifier = Modifier.height(8.dp))
                PrivacyNumberedStep(text = stringResource(id = R.string.privacy_sec5_step1))
                Spacer(modifier = Modifier.height(6.dp))
                PrivacyNumberedStep(text = stringResource(id = R.string.privacy_sec5_step2))
                Spacer(modifier = Modifier.height(6.dp))
                PrivacyNumberedStep(text = stringResource(id = R.string.privacy_sec5_step3))

                Spacer(modifier = Modifier.height(18.dp))

                // Bagian 6
                PrivacySectionHeader(title = stringResource(id = R.string.privacy_sec6_title))
                Text(
                    text = stringResource(id = R.string.privacy_sec6_p1),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp,
                        color = MizanTheme.colors.textSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MizanTheme.colors.surfaceRaised, RoundedCornerShape(6.dp))
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Bagian 7
                PrivacySectionHeader(title = stringResource(id = R.string.privacy_sec7_title))
                PrivacyParagraph(text = stringResource(id = R.string.privacy_sec7_p1))

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MizanTheme.colors.border, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Footer link
                Row(
                    modifier = Modifier
                        .clickable(onClick = onNavigateBack)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = MizanTheme.colors.accent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(id = R.string.desc_kembali_ke_pengaturan),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MizanTheme.colors.accent
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PrivacySectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = MizanTheme.colors.text
        ),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun PrivacyParagraph(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MizanTheme.colors.text
        )
    )
}

@Composable
private fun PrivacyBulletPoint(text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                color = MizanTheme.colors.textSecondary
            ),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = MizanTheme.colors.text
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PrivacyNumberedStep(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MizanTheme.colors.text
        ),
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun PrivacyCodeBlock(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = MizanTheme.colors.accent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(MizanTheme.colors.surfaceRaised, RoundedCornerShape(4.dp))
            .padding(8.dp)
    )
}
