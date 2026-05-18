package com.example.wishlot.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Coral,
    onPrimary = Color.White,
    primaryContainer = CoralContainer,
    onPrimaryContainer = TextPrimaryDark,
    secondary = Gold,
    onSecondary = Midnight,
    background = Midnight,
    onBackground = TextPrimaryDark,
    surface = CreamSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = CreamVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = OutlineDark,
    tertiary = SuccessMint,
    onTertiary = Color.White,
    error = ErrorCoral,
    onError = Color.Black,
)

@Composable
fun WishlotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content,
    )
}
