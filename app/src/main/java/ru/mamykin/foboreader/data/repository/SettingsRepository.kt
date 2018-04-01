package ru.mamykin.foboreader.data.repository

import ru.mamykin.foboreader.data.storage.SettingsStorage
import ru.mamykin.foboreader.ui.global.UiUtils
import javax.inject.Inject

class SettingsRepository @Inject constructor(
        private val settingsStorage: SettingsStorage
) {
    fun getNightThemeEnabled(): Boolean {
        return settingsStorage.nightThemeEnabled
    }

    fun getManualBrightnessEnabled(): Boolean {
        return settingsStorage.manualBrightnessEnabled
    }

    fun getManualBrightnessValue(): Float {
        return settingsStorage.manualBrightnessValue
    }

    fun getBookTextSize(): Int {
        return settingsStorage.bookTextSize
    }

    fun getDropboxAccount(): String? {
        return settingsStorage.dropboxAccount
    }

    fun enableNightTheme(enable: Boolean) {
        settingsStorage.nightThemeEnabled = enable
        UiUtils.enableNightMode(enable)
    }

    fun enableAutoBrightness(auto: Boolean) {
        settingsStorage.manualBrightnessEnabled = !auto
    }

    fun changeBrightness(value: Float) {
        settingsStorage.manualBrightnessValue = value
    }

    fun logoutDropbox() {
        settingsStorage.dropboxAccount = null
    }
}