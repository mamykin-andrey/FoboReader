package ru.mamykin.foboreader.domain.settings

import ru.mamykin.foboreader.data.repository.settings.SettingsRepository
import javax.inject.Inject

class SettingsInteractor @Inject constructor(
        private val repository: SettingsRepository
) {
    fun changeBrightness(percentage: Int) {
        val progressValue = percentage / 100f
        repository.changeBrightness(progressValue)
    }

    fun enableNightTheme(enable: Boolean) {
        repository.enableNightTheme(enable)
    }

    fun enableAutoBrightness(enable: Boolean) {
        repository.enableAutoBrightness(enable)
    }

    fun logoutDropbox() {
        repository.logoutDropbox()
    }

    fun isNightThemeEnabled(): Boolean {
        return repository.isNightThemeEnabled()
    }

    fun isManualBrightnessEnabled(): Boolean {
        return repository.isManualBrightnessEnabled()
    }

    fun getManualBrightnessValue(): Int {
        return (repository.getManualBrightnessValue() * 100).toInt()
    }

    fun getReadTextSize(): Int {
        return repository.getBookTextSize()
    }

    fun getDropboxAccount(): String? {
        return repository.getDropboxAccount()
    }
}