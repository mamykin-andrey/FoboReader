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
        val manualBrightnessValue = repository.getManualBrightnessValue()
        val readTextSize = repository.getBookTextSize()
        val dropboxAccount = repository.getDropboxAccount()

        val appSettings = AppSettingsEntity(
                nightThemeEnabled,
                manualBrightnessEnabled,
                manualBrightnessValue,
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

    fun changeBrightness(value: Int): Completable {
        return repository.changeBrightness(value)
    }

    fun logoutDropbox(): Completable {
        return repository.logoutDropbox()
    }
}