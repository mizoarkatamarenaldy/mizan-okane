package com.mizoar.mizanokane.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mizoar.mizanokane.ui.navigation.Screen
import com.mizoar.mizanokane.ui.theme.MizanTheme

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MizanTheme.colors.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MizanTheme.colors.border
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Screen.items.forEach { screen ->
                    val isSelected = screen.route == currentRoute
                    if (screen == Screen.Timbangan) {
                        CenterNavItem(
                            screen = screen,
                            isSelected = isSelected,
                            onClick = { onTabSelected(screen) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        StandardNavItem(
                            screen = screen,
                            isSelected = isSelected,
                            onClick = { onTabSelected(screen) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StandardNavItem(
    screen: Screen,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MizanTheme.colors
    val contentColor = if (isSelected) colors.accent else colors.textSecondary

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = screen.iconRes),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = contentColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = screen.titleRes),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = contentColor
        )
    }
}

@Composable
private fun CenterNavItem(
    screen: Screen,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MizanTheme.colors
    val iconTint = if (isSelected) colors.onAccent else colors.textSecondary
    val labelColor = if (isSelected) colors.accent else colors.textSecondary
    val circleBg = if (isSelected) colors.accent else colors.surfaceRaised
    val circleBorder = if (isSelected) colors.accent else colors.border

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.padding(bottom = 9.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(circleBg)
                    .border(width = 1.dp, color = circleBorder, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = screen.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = iconTint
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(id = screen.titleRes),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = labelColor
            )
        }
    }
}
