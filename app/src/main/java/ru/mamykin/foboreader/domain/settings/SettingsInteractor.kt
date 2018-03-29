package ru.mamykin.foboreader.domain.settings

import ru.mamykin.foboreader.data.repository.SettingsRepository
import rx.Completable
import rx.Single
import javax.inject.Inject

class SettingsInteractor @Inject constructor(
        private val repository: SettingsRepository
) {
    fun getSettings(): Single<AppSettingsEntity> {
        val nightThemeEnabled = repository.getNightThemeEnabled()
        val manualBrightnessEnabled = repository.getManualBrightnessEnabled()
        val readTextSize = repository.getBookTextSize()
        val dropboxAccount = repository.getDropboxAccount()

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

    fun enableNightTheme(enable: Boolean): Completable {
        return repository.enableNightTheme(enable)
    }

    fun enableAutoBrightness(enable: Boolean): Single<Boolean> {
        return repository.enableAutoBrightness(enable)
    }

    fun changeBrightness(percentage: Int): Completable {
        val progressValue = percentage / 100f
        return repository.changeBrightness(progressValue)
    }

    fun logoutDropbox(): Completable {
        return repository.logoutDropbox()
    }
}