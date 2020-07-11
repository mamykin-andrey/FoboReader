package ru.mamykin.foboreader.settings.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.mamykin.foboreader.core.data.storage.SettingsStorage
import ru.mamykin.foboreader.settings.domain.entity.Settings

@ExperimentalCoroutinesApi
@FlowPreview
class SettingsInteractor(
    private val settings: SettingsStorage
) {
    private val settingsChannel = BroadcastChannel<Settings>(1)
    val settingsFlow get() = settingsChannel.asFlow()

    suspend fun loadData() {
        updateSettings()
    }

    suspend fun increaseTextSize() {
        changeTextSize(settings.readTextSize + 1)
    }

    suspend fun decreaseTextSize() {
        changeTextSize(settings.readTextSize - 1)
    }

    private suspend fun changeTextSize(newSize: Int) {
        newSize.takeIf { it in 10..30 }
            ?.let { settings.readTextSize = it }
            ?.also { updateSettings() }
    }

    suspend fun enableAutoBrightness(enabled: Boolean) {
        settings.isAutoBrightness = enabled
        updateSettings()
    }

    suspend fun enableNightTheme(enabled: Boolean) {
        settings.isNightTheme = enabled
        updateSettings()
    }

    suspend fun changeBrightness(value: Int) {
        settings.brightness = value
        updateSettings()
    }

    private suspend fun updateSettings() {
        settingsChannel.send(Settings(
            settings.isNightTheme,
            settings.isAutoBrightness,
            settings.brightness,
            settings.readTextSize
        ))
    }
}