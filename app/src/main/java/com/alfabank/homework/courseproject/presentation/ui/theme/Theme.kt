package com.alfabank.homework.courseproject.presentation.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    // Основной акцентный цвет
    primary = ProjectYellow,
    onPrimary = Color.Black,                  // чёрный текст на жёлтом фоне

    // Вторичный акцент (можно использовать Grey2)
    secondary = Grey2,
    onSecondary = Grey1,

    // Третичный цвет (например, для менее важных элементов)
    tertiary = Grey4,
    onTertiary = Grey1,

    // Фон всего приложения
    background = BackgroundGrey,
    onBackground = Grey1,                      // основной текст на фоне

    // Поверхности (карточки, диалоги)
    surface = Grey3,
    onSurface = Grey1,                         // текст на поверхности

    // Вариант поверхности (например, для разделителей)
    surfaceVariant = Grey4,
    onSurfaceVariant = Grey2,                   // текст на варианте поверхности
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

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
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
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