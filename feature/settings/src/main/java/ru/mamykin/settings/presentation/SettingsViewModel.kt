package ru.mamykin.settings.presentation

import kotlinx.coroutines.launch
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.mvvm.BaseViewModel

class SettingsViewModel constructor(
        private val settings: SettingsStorage
) : BaseViewModel<SettingsViewModel.ViewState, SettingsViewModel.Action, String>(
        ViewState()
) {
    fun loadSettings() = launch {
        onAction(Action.SettingsLoaded(
                settings.isNightTheme,
                settings.brightness,
                settings.readTextSize
        ))
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.SettingsLoaded -> state.copy(
                nightTheme = action.nightTheme,
                manualBrightness = action.brightness == null,
                brightness = action.brightness ?: 100,
                textSize = action.textSize.toString()
        )
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.BrightnessChanged -> settings.brightness = event.value
            is Event.NightThemeEnabled -> settings.isNightTheme = event.value
            is Event.BrightAutoEnabled -> event.value.takeIf { it }?.let { settings.brightness = null }
            is Event.TextSizeDecreased -> {
                settings.readTextSize
                        .takeIf { it > 10 }
                        ?.let { settings.readTextSize = it - 1 }
            }
            is Event.TextSizeIncreased -> {
                settings.readTextSize
                        .takeIf { it < 30 }
                        ?.let { settings.readTextSize = it + 1 }
            }
        }
        loadSettings()
    }

    sealed class Event {
        data class BrightnessChanged(val value: Int) : Event()
        data class NightThemeEnabled(val value: Boolean) : Event()
        data class BrightAutoEnabled(val value: Boolean) : Event()
        object TextSizeDecreased : Event()
        object TextSizeIncreased : Event()
    }

    sealed class Action {
        data class SettingsLoaded(
                val nightTheme: Boolean,
                val brightness: Int?,
                val textSize: Int
        ) : Action()
    }

    data class ViewState(
            val nightTheme: Boolean = false,
            val manualBrightness: Boolean = false,
            val brightness: Int = 0,
            val textSize: String = ""
    )
}