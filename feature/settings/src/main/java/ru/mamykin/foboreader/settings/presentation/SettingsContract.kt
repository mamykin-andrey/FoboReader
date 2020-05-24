package ru.mamykin.foboreader.settings.presentation

import ru.mamykin.foboreader.settings.domain.entity.Settings

sealed class SettingsEvent {
    data class BrightnessChanged(val brightness: Int) : SettingsEvent()
    data class NightThemeChanged(val nightTheme: Boolean) : SettingsEvent()
    data class AutoBrightnessChanged(val autoBrightness: Boolean) : SettingsEvent()
    object IncreaseTextSizeClicked : SettingsEvent()
    object DecreaseTextSizeClicked : SettingsEvent()
}

sealed class SettingsAction {
    data class SettingsLoaded(val settings: Settings) : SettingsAction()
}

data class ViewState(
    val isNightTheme: Boolean = false,
    val isAutoBrightness: Boolean = false,
    val brightness: Int = 0,
    val contentTextSize: Int = 0
)