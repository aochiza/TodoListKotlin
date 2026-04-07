package com.example.todolist.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Тёмная тема с современными цветами
private val DarkColorScheme = darkColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = GreenSecondary,
    onSecondary = Color.Black,
    secondaryContainer = GreenSecondaryDark,
    onSecondaryContainer = Color.Black,
    tertiary = AccentYellow,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = SurfaceColor,
    onSurface = TextPrimary,
    surfaceVariant = CardDark,
    error = AccentRed,
    onError = Color.White
)

// Светлая тема с современными цветами
private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealPrimaryLight,
    onPrimaryContainer = Color.Black,
    secondary = GreenSecondaryDark,
    onSecondary = Color.Black,
    secondaryContainer = GreenSecondaryLight,
    onSecondaryContainer = Color.Black,
    tertiary = AccentOrange,
    onTertiary = Color.Black,
    background = LightBackground,
    onBackground = TextDarkPrimary,
    surface = SurfaceLight,
    onSurface = TextDarkPrimary,
    surfaceVariant = CardLight,
    error = AccentRed,
    onError = Color.White
)

@Composable
fun TodolistTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Отключаем динамические цвета для единообразия
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}