package com.mizoar.mizanokane.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Palet Gelap (dev/DESIGN.md)
val DarkBg = Color(0xFF0F1117)
val DarkSurface = Color(0xFF181A24)
val DarkSurfaceRaised = Color(0xFF1F2232)
val DarkBorder = Color(0xFF2A2D3A)
val DarkText = Color(0xFFE4E4E8)
val DarkTextSecondary = Color(0xFF9194A1)
val DarkAccent = Color(0xFF2ECC8F)
val DarkOnAccent = Color(0xFF0F1117)

// Palet Terang (dev/DESIGN.md)
val LightBg = Color(0xFFF5F5F7)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceRaised = Color(0xFFF0F0F2)
val LightBorder = Color(0xFFD8D8DC)
val LightText = Color(0xFF1A1A1E)
val LightTextSecondary = Color(0xFF6B6D78)
val LightAccent = Color(0xFF24A574)
val LightOnAccent = Color(0xFFFFFFFF)

// Warna Makna (dev/DESIGN.md)
val ColorExpense = Color(0xFFFF453A)
val ColorTransfer = Color(0xFF4A9EFF)
val ColorBudgetWarning = Color(0xFFF5A623)

@Immutable
data class MizanColors(
    val bg: Color,
    val surface: Color,
    val surfaceRaised: Color,
    val border: Color,
    val text: Color,
    val textSecondary: Color,
    val accent: Color,
    val onAccent: Color,
    val expense: Color,
    val transfer: Color,
    val budgetWarning: Color
) {
    val danger: Color
        get() = expense

    val budgetWarn: Color
        get() = budgetWarning
}

val DarkMizanColorPalette = MizanColors(
    bg = DarkBg,
    surface = DarkSurface,
    surfaceRaised = DarkSurfaceRaised,
    border = DarkBorder,
    text = DarkText,
    textSecondary = DarkTextSecondary,
    accent = DarkAccent,
    onAccent = DarkOnAccent,
    expense = ColorExpense,
    transfer = ColorTransfer,
    budgetWarning = ColorBudgetWarning
)

val LightMizanColorPalette = MizanColors(
    bg = LightBg,
    surface = LightSurface,
    surfaceRaised = LightSurfaceRaised,
    border = LightBorder,
    text = LightText,
    textSecondary = LightTextSecondary,
    accent = LightAccent,
    onAccent = LightOnAccent,
    expense = ColorExpense,
    transfer = ColorTransfer,
    budgetWarning = ColorBudgetWarning
)

val LocalMizanColors = staticCompositionLocalOf { DarkMizanColorPalette }

val MizanColors.danger: Color
    get() = expense

val MizanColors.budgetWarn: Color
    get() = budgetWarning

