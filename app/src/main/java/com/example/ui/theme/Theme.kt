package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MedRealmDarkColorScheme = darkColorScheme(
    primary = MedicalTeal,
    onPrimary = MidnightNavy,
    primaryContainer = MedicalTealContainer,
    onPrimaryContainer = MedicalTealOnContainer,
    secondary = RewardGold,
    onSecondary = MidnightNavy,
    secondaryContainer = RewardGoldContainer,
    onSecondaryContainer = RewardGold,
    tertiary = RoyalPurpleLight,
    onTertiary = WarmCream,
    background = MidnightNavy,
    onBackground = WarmCream,
    surface = DeepNavySurface,
    onSurface = WarmCream,
    surfaceVariant = NavyElevated,
    onSurfaceVariant = Color(0xFFCAD4DF),
    error = ErrorRed,
    onError = Color.White
)

private val MedRealmLightColorScheme = lightColorScheme(
    primary = MedicalTealDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC7F3ED),
    onPrimaryContainer = Color(0xFF003833),
    secondary = RewardGoldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFCEECB),
    onSecondaryContainer = Color(0xFF382900),
    tertiary = RoyalPurple,
    onTertiary = Color.White,
    background = WarmCreamSurface,
    onBackground = MidnightNavy,
    surface = Color.White,
    onSurface = MidnightNavy,
    surfaceVariant = WarmCream,
    onSurfaceVariant = Color(0xFF4A453E),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MedRealmTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MedRealmDarkColorScheme else MedRealmLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
