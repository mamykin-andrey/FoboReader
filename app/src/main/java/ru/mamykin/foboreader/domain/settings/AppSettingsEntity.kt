package ru.mamykin.foboreader.domain.settings

data class AppSettingsEntity(
        val nightThemeEnabled: Boolean,
        val manualBrightnessEnabled: Boolean,
        val manualBrightnessValue: Int,
        val readTextSize: Int,
        val dropboxAccount: String?
)