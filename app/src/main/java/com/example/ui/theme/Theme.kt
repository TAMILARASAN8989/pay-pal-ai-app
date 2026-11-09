package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GuardianColorScheme = darkColorScheme(
    primary = GuardianEmerald,
    onPrimary = Color(0xFF022C22),
    primaryContainer = GuardianEmeraldContainer,
    onPrimaryContainer = GuardianEmeraldLight,

    secondary = GuardianCyan,
    onSecondary = Color(0xFF083344),
    secondaryContainer = Color(0xFF164E63),
    onSecondaryContainer = GuardianCyanLight,

    tertiary = GuardianBlue,
    onTertiary = Color(0xFF0F172A),
    tertiaryContainer = GuardianBlueDark,
    onTertiaryContainer = Color(0xFFDBEAFE),

    background = GuardianDarkBg,
    onBackground = GuardianTextPrimary,

    surface = GuardianDarkSurface,
    onSurface = GuardianTextPrimary,
    surfaceVariant = GuardianSurfaceVariant,
    onSurfaceVariant = GuardianTextSecondary,

    outline = GuardianBorder,
    outlineVariant = GuardianBorderLight,

    error = GuardianRose,
    onError = Color(0xFF450A0A),
    errorContainer = GuardianRoseContainer,
    onErrorContainer = Color(0xFFFFE4E6)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force premium dark fintech cyber aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GuardianColorScheme,
        typography = Typography,
        content = content
    )
}
