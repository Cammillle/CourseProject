package com.alfabank.homework.courseproject.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    // Основной акцентный цвет
    primary = ProjectYellow,
    onPrimary = BackgroundGrey,

    secondary = Grey2,
    onSecondary = Grey1,

    tertiary = Grey4,
    onTertiary = Grey1,

    background = BackgroundGrey,
    onBackground = Grey1,

    // Поверхности (карточки, диалоги)
    surface = Grey3,
    onSurface = Grey1,

    // Вариант поверхности
    surfaceVariant = Grey4,
    onSurfaceVariant = Grey2,
)

private val LightColorScheme = lightColorScheme(
    primary = ProjectYellow,
    onPrimary = Color.Black,

    secondary = Grey2,
    onSecondary = Grey1,

    tertiary = Grey4,
    onTertiary = Grey1,

    background = BackgroundGrey,
    onBackground = Grey1,

    surface = Grey3,
    onSurface = Grey1,

    surfaceVariant = Grey4,
    onSurfaceVariant = Grey2,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CourseProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}