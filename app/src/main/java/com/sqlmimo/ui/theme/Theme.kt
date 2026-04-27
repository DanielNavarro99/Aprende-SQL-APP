package com.sqlmimo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Brand = Color(0xFF4F46E5)
val BrandDark = Color(0xFF3730A3)
val BrandLight = Color(0xFFEEF2FF)
val SuccessGreen = Color(0xFF3B6D11)
val SuccessGreenBg = Color(0xFFEAF3DE)
val ErrorRed = Color(0xFFA32D2D)
val ErrorRedBg = Color(0xFFFCEBEB)
val StreakOrange = Color(0xFFC2410C)
val StreakBg = Color(0xFFFFF7ED)
val CodeBg = Color(0xFFF8F7FF)
val KeywordColor = Color(0xFF4F46E5)
val StringColor = Color(0xFF059669)
val NumberColor = Color(0xFFD97706)
val CommentColor = Color(0xFF9CA3AF)

private val LightColors = lightColorScheme(
    primary = Brand,
    onPrimary = Color.White,
    primaryContainer = BrandLight,
    onPrimaryContainer = BrandDark,
    background = Color(0xFFF9F9F9),
    surface = Color.White,
    onBackground = Color(0xFF111827),
    onSurface = Color(0xFF111827),
    outline = Color(0xFFE5E7EB),
    outlineVariant = Color(0xFFE5E7EB)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF818CF8),
    onPrimary = Color.White,
    background = Color(0xFF111827),
    surface = Color(0xFF1F2937),
    onBackground = Color.White,
    onSurface = Color.White,
    outline = Color(0xFF374151),
    outlineVariant = Color(0xFF374151)
)

@Composable
fun SQLMimoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}