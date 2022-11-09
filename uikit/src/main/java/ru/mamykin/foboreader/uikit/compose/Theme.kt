package ru.mamykin.foboreader.uikit.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DarkColors.Primary,
    primaryVariant = DarkColors.PrimaryVariant,
    secondary = DarkColors.Secondary,
    secondaryVariant = DarkColors.SecondaryVariant,
    background = DarkColors.Background,
    surface = DarkColors.Surface,
    error = DarkColors.Error,
    onPrimary = DarkColors.OnPrimary,
    onSecondary = DarkColors.OnSecondary,
    onBackground = DarkColors.OnBackground,
    onSurface = DarkColors.OnSurface,
    onError = DarkColors.OnError,
)

private val LightColorPalette = lightColors(
    primary = LightColors.Primary,
    primaryVariant = LightColors.PrimaryVariant,
    secondary = LightColors.Secondary,
    secondaryVariant = LightColors.SecondaryVariant,
    background = LightColors.Background,
    surface = LightColors.Surface,
    error = LightColors.Error,
    onPrimary = LightColors.OnPrimary,
    onSecondary = LightColors.OnSecondary,
    onBackground = LightColors.OnBackground,
    onSurface = LightColors.OnSurface,
    onError = LightColors.OnError,
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