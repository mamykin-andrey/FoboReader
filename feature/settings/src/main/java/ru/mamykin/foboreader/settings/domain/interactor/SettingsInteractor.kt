package ru.mamykin.foboreader.settings.domain.interactor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.mamykin.foboreader.core.data.storage.SettingsStorage
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

@ExperimentalCoroutinesApi
@FlowPreview
class SettingsInteractor(
    private val settings: SettingsStorage
) {
    private val settingsChannel = BroadcastChannel<List<SettingsItem>>(1)
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
        settingsChannel.send(
            listOf(
                SettingsItem.NightTheme(settings.isNightTheme),
                SettingsItem.Brightness(settings.brightness),
                SettingsItem.ReadTextSize(settings.readTextSize),
                SettingsItem.TranslationColor(settings.translationColorCode ?: "#000000")
            )
        )
    }
}