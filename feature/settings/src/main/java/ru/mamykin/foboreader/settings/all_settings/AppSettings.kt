package ru.mamykin.foboreader.settings.all_settings

internal data class AppSettings(
    val isNightThemeEnabled: Boolean,
    val backgroundColor: String,
    val textColor: String,
    val translationColor: String,
    val textSize: Int,
    val languageName: String,
    val isVibrationEnabled: Boolean,
)