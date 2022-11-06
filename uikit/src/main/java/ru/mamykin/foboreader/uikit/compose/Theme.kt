package ru.mamykin.foboreader.uikit.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Light.ColorPrimary,
    primaryVariant = Light.ColorPrimaryInversed,
    secondary = Light.ColorSecondary
)

private val LightColorPalette = lightColors(
    primary = Dark.ColorPrimary,
    primaryVariant = Dark.ColorPrimaryInversed,
    secondary = Dark.ColorSecondary,
    // background = Color.White,
    // surface = Color.White,
    // onPrimary = Color.White,
    // onSecondary = Color.Black,
    // onBackground = Color.Black,
    // onSurface = Color.Black,
)

@Composable
fun FoboReaderTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}