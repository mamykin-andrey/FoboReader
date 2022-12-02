package ru.mamykin.foboreader.settings.domain.model

// TODO: Add a separate class for working with colors
internal data class AppSettings(
    val isNightThemeEnabled: Boolean,
    val backgroundColor: String,
    val translationColor: String,
    val textSize: Int,
    val languageName: String,
    val isVibrationEnabled: Boolean,
)