package ru.mamykin.foreignbooksreader.presentation.settings

import android.content.Context
import android.support.v7.app.AppCompatDelegate

import com.arellomobile.mvp.InjectViewState

import org.greenrobot.eventbus.EventBus

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.common.events.RestartEvent
import ru.mamykin.foreignbooksreader.data.storage.PreferencesManager
import ru.mamykin.foreignbooksreader.data.storage.PreferenceNames
import ru.mamykin.foreignbooksreader.presentation.global.BasePresenter

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class SettingsPresenter : BasePresenter<SettingsView>(), PreferenceNames {
    @Inject
    lateinit var context: Context
    @Inject
    lateinit var pm: PreferencesManager

    init {
        ReaderApp.component.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadSettings()
    }

    fun loadSettings() {
        viewState.setNightThemeEnabled(pm!!.getBoolean(PreferenceNames.Companion.NIGHT_THEME_PREF))
        viewState.setAutoBrightnessChecked(pm!!.getBoolean(PreferenceNames.Companion.BRIGHTNESS_AUTO_PREF, true))
        viewState.setBrightnessControlEnabled(!pm!!.getBoolean(PreferenceNames.Companion.BRIGHTNESS_AUTO_PREF))
        viewState.setBrightnessPos((pm!!.getFloat(PreferenceNames.Companion.BRIGHTNESS_PREF, 1f) * 100).toInt())
        viewState.setContentSizeText(pm!!.getString(PreferenceNames.CONTENT_TEXT_SIZE_PREF, "16")!!)
        viewState.setDropboxAccount(pm!!.getString(PreferenceNames.Companion.DROPBOX_EMAIL_PREF)!!)
    }

    /**
     * Переключаем тему на ночную
     * @param isChecked true, если выбрана ночная тема
     */
    fun onNightThemeCheckedChanged(isChecked: Boolean) {
        if (isChecked != UiUtils.nightMode) {
            EventBus.getDefault().postSticky(RestartEvent())

            pm!!.putBoolean(PreferenceNames.Companion.NIGHT_THEME_PREF, isChecked)
            AppCompatDelegate.setDefaultNightMode(if (isChecked)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO)
            viewState.restartActivity()
        }
    }

    /**
     * Переключаем автоматическую регулировку яркости
     * @param isChecked true, если выбрана ручная регулировка
     */
    fun onBrightnessAutoCheckedChanged(isChecked: Boolean) {
        if (isChecked != pm!!.getBoolean(PreferenceNames.BRIGHTNESS_AUTO_PREF)) {
            pm!!.putBoolean(PreferenceNames.Companion.BRIGHTNESS_AUTO_PREF, isChecked)
            viewState.setAutoBrightnessChecked(isChecked)
            viewState.setBrightnessControlEnabled(!isChecked)
            viewState.setupBrightness()
        }
    }

    /**
     * Устанавливаем текущую яркость
     * @param progress значение прогресса, от 0 до 100
     */
    fun onBrightnessProgressChanged(progress: Int) {
        if (!pm!!.getBoolean(PreferenceNames.BRIGHTNESS_AUTO_PREF)) {
            val progressF = progress / 100f
            pm!!.putFloat(PreferenceNames.Companion.BRIGHTNESS_PREF, progressF)
            viewState.setupBrightness()
        }
    }

    /**
     * Отображаем диалог выхода из аккаунта Dropbox
     */
    fun onDropboxLogoutClick() {
        if (pm!!.contains(PreferenceNames.Companion.DROPBOX_TOKEN_PREF)) {
            viewState.showDropboxLogoutDialog()
        }
    }

    /**
     * Выходим из аккаунта Dropbox
     */
    fun onDropboxLogoutPositive() {
        // Т.к. в Dropbox нет нормального метода для выхода из аккаунта
        pm!!.putBoolean(PreferenceNames.Companion.DROPBOX_LOGOUT_PREF, true)
        pm!!.removeValue(PreferenceNames.Companion.DROPBOX_TOKEN_PREF)
        pm!!.removeValue(PreferenceNames.Companion.DROPBOX_EMAIL_PREF)
        viewState.setDropboxAccount(null!!)
    }
}