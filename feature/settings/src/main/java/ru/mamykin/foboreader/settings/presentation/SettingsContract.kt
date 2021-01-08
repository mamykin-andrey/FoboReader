package ru.mamykin.foboreader.settings.presentation

import ru.mamykin.foboreader.settings.domain.model.SettingsItem

sealed class Event {
    data class BrightnessChanged(val brightness: Int) : Event()
    data class NightThemeChanged(val isEnabled: Boolean) : Event()
    object IncreaseTextSizeClicked : Event()
    object DecreaseTextSizeClicked : Event()
    object SelectReadColorClicked : Event()
    object SelectAppLanguage : Event()
    data class UseVibrationChanged(val enabled: Boolean) : Event()
}

sealed class Action {
    data class SettingsLoaded(val settings: List<SettingsItem>) : Action()
}

data class ViewState(
    val isLoading: Boolean = false,
    val settings: List<SettingsItem>? = null
)