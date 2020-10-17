package ru.mamykin.foboreader.settings.presentation

import ru.mamykin.foboreader.settings.domain.model.Settings

sealed class Event {
    data class BrightnessChanged(val brightness: Int) : Event()
    data class NightThemeChanged(val nightTheme: Boolean) : Event()
    data class AutoBrightnessChanged(val autoBrightness: Boolean) : Event()
    object IncreaseTextSizeClicked : Event()
    object DecreaseTextSizeClicked : Event()
    object SelectReadColorClicked : Event()
}

sealed class Effect

sealed class Action {
    data class SettingsLoaded(val settings: Settings) : Action()
}

data class ViewState(
    val isLoading: Boolean = false,
    val settings: Settings? = null
)