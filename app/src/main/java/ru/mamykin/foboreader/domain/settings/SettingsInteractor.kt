package ru.mamykin.foboreader.domain.settings

import ru.mamykin.foboreader.data.repository.SettingsRepository
import rx.Completable
import rx.Single
import javax.inject.Inject

class SettingsInteractor @Inject constructor(
        private val repository: SettingsRepository
) {
    fun getSettings(): Single<AppSettingsEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        viewState.setNightThemeEnabled(pm!!.getBoolean(PreferenceNames.Companion.NIGHT_THEME_PREF))
//        viewState.setAutoBrightnessChecked(pm!!.getBoolean(PreferenceNames.Companion.BRIGHTNESS_AUTO_PREF, true))
//        viewState.setBrightnessControlEnabled(!pm!!.getBoolean(PreferenceNames.Companion.BRIGHTNESS_AUTO_PREF))
//        viewState.setBrightnessPos((pm!!.getFloat(PreferenceNames.Companion.BRIGHTNESS_PREF, 1f) * 100).toInt())
//        viewState.setContentSizeText(pm!!.getString(PreferenceNames.CONTENT_TEXT_SIZE_PREF, "16")!!)
//        viewState.setDropboxAccount(pm!!.getString(PreferenceNames.Companion.DROPBOX_EMAIL_PREF)!!)
    }

    fun enableNightTheme(enable: Boolean): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        if (isChecked != UiUtils.nightMode) {
//            EventBus.getDefault().postSticky(RestartEvent())
//
//            pm!!.putBoolean(PreferenceNames.Companion.NIGHT_THEME_PREF, isChecked)
//            AppCompatDelegate.setDefaultNightMode(if (isChecked)
//                AppCompatDelegate.MODE_NIGHT_YES
//            else
//                AppCompatDelegate.MODE_NIGHT_NO)
//            viewState.restartActivity()
//        }
    }

    fun enableAutoBrightness(checked: Boolean): Single<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        if (isChecked != pm!!.getBoolean(PreferenceNames.BRIGHTNESS_AUTO_PREF)) {
//            pm!!.putBoolean(PreferenceNames.Companion.BRIGHTNESS_AUTO_PREF, isChecked)
//            viewState.setAutoBrightnessChecked(isChecked)
//            viewState.setBrightnessControlEnabled(!isChecked)
//            viewState.setupBrightness()
//        }
    }

    fun changeBrightness(progress: Int): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        if (!pm!!.getBoolean(PreferenceNames.BRIGHTNESS_AUTO_PREF)) {
//            val progressF = progress / 100f
//            pm!!.putFloat(PreferenceNames.Companion.BRIGHTNESS_PREF, progressF)
    }

    fun logoutDropbox(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        // Т.к. в Dropbox нет нормального метода для выхода из аккаунта
//        pm!!.putBoolean(PreferenceNames.Companion.DROPBOX_LOGOUT_PREF, true)
//        pm!!.removeValue(PreferenceNames.Companion.DROPBOX_TOKEN_PREF)
//        pm!!.removeValue(PreferenceNames.Companion.DROPBOX_EMAIL_PREF)
    }
}