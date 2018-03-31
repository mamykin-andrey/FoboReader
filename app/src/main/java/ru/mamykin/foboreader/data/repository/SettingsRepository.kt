package ru.mamykin.foboreader.data.repository

import ru.mamykin.foboreader.ui.global.UiUtils
import ru.mamykin.foboreader.data.storage.PreferenceNames
import ru.mamykin.foboreader.data.storage.PreferencesManager
import rx.Completable
import rx.Single
import javax.inject.Inject

class SettingsRepository @Inject constructor(
        private val preferencesManager: PreferencesManager
) {
    fun getNightThemeEnabled(): Boolean {
        return preferencesManager.getBoolean(PreferenceNames.NIGHT_THEME_PREF)
    }

    fun getManualBrightnessEnabled(): Boolean {
        return preferencesManager.getBoolean(PreferenceNames.BRIGHTNESS_AUTO_PREF)
    }

    fun getManualBrightnessValue(): Float {
        return preferencesManager.getFloat(PreferenceNames.BRIGHTNESS_PREF, 1f)
    }

    fun getBookTextSize(): Int {
        return preferencesManager.getInt(PreferenceNames.CONTENT_TEXT_SIZE_PREF, 16)
    }

    fun getDropboxAccount(): String {
        return preferencesManager.getString(PreferenceNames.DROPBOX_EMAIL_PREF)!!
    }
    
    fun enableNightTheme(enable: Boolean): Completable {
        return Completable.fromCallable {
            UiUtils.enableNightMode(enable)
            preferencesManager.putBoolean(PreferenceNames.NIGHT_THEME_PREF, enable)
        }
    }

    fun enableAutoBrightness(enable: Boolean): Single<Boolean> {
        // TODO: ну такое
        preferencesManager.putBoolean(PreferenceNames.Companion.BRIGHTNESS_AUTO_PREF, enable)
        return Single.just(enable)
    }

    fun changeBrightness(value: Float): Completable {
        return Completable.fromCallable {
            preferencesManager.putFloat(PreferenceNames.Companion.BRIGHTNESS_PREF, value)
        }
    }

    fun logoutDropbox(): Completable {
        return Completable.fromCallable {
            preferencesManager.putBoolean(PreferenceNames.Companion.DROPBOX_LOGOUT_PREF, true)
            preferencesManager.removeValue(PreferenceNames.Companion.DROPBOX_TOKEN_PREF)
            preferencesManager.removeValue(PreferenceNames.Companion.DROPBOX_EMAIL_PREF)
        }
    }
}