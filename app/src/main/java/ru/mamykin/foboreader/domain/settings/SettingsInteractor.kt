package ru.mamykin.foboreader.domain.settings

import ru.mamykin.foboreader.data.repository.SettingsRepository
import rx.Single
import javax.inject.Inject

class SettingsInteractor @Inject constructor(
        private val repository: SettingsRepository
) {
    fun getSettings(): Single<AppSettingsEntity> {
        val nightThemeEnabled = repository.getNightThemeEnabled()
        val readTextSize = repository.getBookTextSize()
        val dropboxAccount = repository.getDropboxAccount()

        val manualBrightnessEnabled = repository.getManualBrightnessEnabled()
        val manualBrightnessValue = repository.getManualBrightnessValue()
        val manualBrightnessPercentage = (manualBrightnessValue * 100).toInt()

        val appSettings = AppSettingsEntity(
                nightThemeEnabled,
                manualBrightnessEnabled,
                manualBrightnessPercentage,
                readTextSize,
                dropboxAccount
        )
        return Single.just(appSettings)
    }

    fun changeBrightness(percentage: Int) {
        val progressValue = percentage / 100f
        repository.changeBrightness(progressValue)
    }

    fun enableNightTheme(enable: Boolean) = repository.enableNightTheme(enable)

    fun enableAutoBrightness(enable: Boolean) = repository.enableAutoBrightness(enable)

    fun logoutDropbox() = repository.logoutDropbox()
}