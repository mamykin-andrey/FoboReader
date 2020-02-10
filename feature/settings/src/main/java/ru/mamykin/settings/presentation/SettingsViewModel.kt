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
                nightTheme = settings.isNightTheme,
                brightness = settings.brightness,
                textSize = settings.readTextSize
        ))
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.SettingsLoaded -> state.copy(
                nightTheme = action.nightTheme,
                manualBrightness = action.brightness == null,
                brightness = action.brightness ?: 100,
                textSize = action.textSize
        )
    }

    fun increaseTextSize() {
        changeTextSize(settings.readTextSize + 1)
    }

    fun decreaseTextSize() {
        changeTextSize(settings.readTextSize - 1)
    }

    private fun changeTextSize(newSize: Int) {
        newSize.takeIf { it in 10..30 }
                ?.let { settings.readTextSize = it }
                ?.also { loadSettings() }
    }

    fun switchAutoBrightness(value: Boolean) {
        value.takeIf { it }?.let { settings.brightness = null }
        loadSettings()
    }

    fun changeTheme(value: Boolean) {
        settings.isNightTheme = value
        loadSettings()
    }

    fun changeBrightness(value: Int) {
        settings.brightness = value
        loadSettings()
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
            val textSize: Int = 0
    )
}