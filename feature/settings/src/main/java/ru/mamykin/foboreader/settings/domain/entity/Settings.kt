package ru.mamykin.foboreader.settings.domain.entity

data class Settings(
    val isNightTheme: Boolean,
    val isAutoBrightness: Boolean,
    val brightness: Int,
    val contentTextSize: Int
)