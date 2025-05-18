package ru.mamykin.foboreader.uikit.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = DarkColors.Primary,
    primaryContainer = DarkColors.PrimaryVariant,
    secondary = DarkColors.Secondary,
    secondaryContainer = DarkColors.SecondaryVariant,
    background = DarkColors.Background,
    surface = DarkColors.Surface,
    error = DarkColors.Error,
    onPrimary = DarkColors.OnPrimary,
    onSecondary = DarkColors.OnSecondary,
    onBackground = DarkColors.OnBackground,
    onSurface = DarkColors.OnSurface,
    onError = DarkColors.OnError,
)

private val LightColorPalette = lightColorScheme(
    primary = LightColors.Primary,
    primaryContainer = LightColors.PrimaryVariant,
    secondary = LightColors.Secondary,
    secondaryContainer = LightColors.SecondaryVariant,
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
fun FoboReaderTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    MaterialTheme(
        colorScheme = colors,
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}