package ru.mamykin.foboreader.settings.presentation

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.data.SettingsStorage
import ru.mamykin.foboreader.core.mvvm.BaseViewModel

class SettingsViewModel constructor(
        private val settings: SettingsStorage
) : BaseViewModel<SettingsViewModel.ViewState, SettingsViewModel.Action>(
        ViewState()
) {
    fun loadSettings() = launch {
        sendAction(Action.SettingsLoaded(
                nightTheme = settings.isNightTheme,
                autoBrightness = settings.isAutoBrightness,
                brightness = settings.brightness,
                textSize = settings.readTextSize
        ))
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.SettingsLoaded -> state.copy(
                nightTheme = action.nightTheme,
                autoBrightness = action.autoBrightness,
                brightnessValue = action.brightness,
                textSize = action.textSize
        )
        is Action.TextSizeChanged -> state.copy(
                textSize = action.textSize
        )
        is Action.AutoBrightnessChanged -> state.copy(
                autoBrightness = action.autoBrightness
        )
        is Action.NightThemeChanged -> state.copy(
                nightTheme = action.nightTheme
        )
        is Action.BrightnessChanged -> state.copy(
                brightnessValue = action.brightness
        )
    }

    fun increaseTextSize() = changeTextSize(settings.readTextSize + 1)

    fun decreaseTextSize() = changeTextSize(settings.readTextSize - 1)

    private fun changeTextSize(newSize: Int) {
        newSize.takeIf { it in 10..30 }
                ?.let {
                    settings.readTextSize = it
                    sendAction(Action.TextSizeChanged(it))
                }
    }

    fun enableAutoBrightness(enabled: Boolean) {
        settings.isAutoBrightness = enabled
        sendAction(Action.AutoBrightnessChanged(enabled))
    }

    fun enableNightTheme(enabled: Boolean) {
        settings.isNightTheme = enabled
        sendAction(Action.NightThemeChanged(enabled))
    }

    fun changeBrightness(value: Int) {
        settings.brightness = value
        sendAction(Action.BrightnessChanged(value))
    }

    sealed class Action {
        data class SettingsLoaded(
                val nightTheme: Boolean,
                val autoBrightness: Boolean,
                val brightness: Int,
                val textSize: Int
        ) : Action()

        data class TextSizeChanged(val textSize: Int) : Action()
        data class AutoBrightnessChanged(val autoBrightness: Boolean) : Action()
        data class NightThemeChanged(val nightTheme: Boolean) : Action()
        data class BrightnessChanged(val brightness: Int) : Action()
    }

    data class ViewState(
            val nightTheme: Boolean = false,
            val autoBrightness: Boolean = false,
            val brightnessValue: Int = 0,
            val textSize: Int = 0
    )
}