package ru.mamykin.foreignbooksreader.presenters;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.arellomobile.mvp.InjectViewState;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.events.RestartEvent;
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager;
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames;
import ru.mamykin.foreignbooksreader.views.SettingsView;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> implements PreferenceNames {
    @Inject
    protected Context context;
    @Inject
    protected PreferencesManager pm;

    public SettingsPresenter() {
        ReaderApp.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadSettings();
    }

    public void loadSettings() {
        getViewState().setNightThemeEnabled(pm.getBoolean(Companion.getNIGHT_THEME_PREF()));
        getViewState().setAutoBrightnessChecked(pm.getBoolean(Companion.getBRIGHTNESS_AUTO_PREF(), true));
        getViewState().setBrightnessControlEnabled(!pm.getBoolean(Companion.getBRIGHTNESS_AUTO_PREF()));
        getViewState().setBrightnessPos((int) (pm.getFloat(Companion.getBRIGHTNESS_PREF(), 1f) * 100));
        getViewState().setContentSizeText(pm.getString(PreferenceNames.Companion.getCONTENT_TEXT_SIZE_PREF(), "16"));
        getViewState().setDropboxAccount(pm.getString(Companion.getDROPBOX_EMAIL_PREF()));
    }

    /**
     * Переключаем тему на ночную
     * @param isChecked true, если выбрана ночная тема
     */
    public void onNightThemeCheckedChanged(boolean isChecked) {
        if (isChecked != UiUtils.getNightMode()) {
            EventBus.getDefault().postSticky(new RestartEvent());

            pm.putBoolean(Companion.getNIGHT_THEME_PREF(), isChecked);
            AppCompatDelegate.setDefaultNightMode(isChecked
                    ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            getViewState().restartActivity();
        }
    }

    /**
     * Переключаем автоматическую регулировку яркости
     * @param isChecked true, если выбрана ручная регулировка
     */
    public void onBrightnessAutoCheckedChanged(boolean isChecked) {
        if (isChecked != pm.getBoolean(PreferenceNames.Companion.getBRIGHTNESS_AUTO_PREF())) {
            pm.putBoolean(Companion.getBRIGHTNESS_AUTO_PREF(), isChecked);
            getViewState().setAutoBrightnessChecked(isChecked);
            getViewState().setBrightnessControlEnabled(!isChecked);
            getViewState().setupBrightness();
        }
    }

    /**
     * Устанавливаем текущую яркость
     * @param progress значение прогресса, от 0 до 100
     */
    public void onBrightnessProgressChanged(int progress) {
        if (!pm.getBoolean(PreferenceNames.Companion.getBRIGHTNESS_AUTO_PREF())) {
            final float progressF = progress / 100f;
            pm.putFloat(Companion.getBRIGHTNESS_PREF(), progressF);
            getViewState().setupBrightness();
        }
    }

    /**
     * Отображаем диалог выхода из аккаунта Dropbox
     */
    public void onDropboxLogoutClick() {
        if (pm.contains(Companion.getDROPBOX_TOKEN_PREF())) {
            getViewState().showDropboxLogoutDialog();
        }
    }

    /**
     * Выходим из аккаунта Dropbox
     */
    public void onDropboxLogoutPositive() {
        // Т.к. в Dropbox нет нормального метода для выхода из аккаунта
        pm.putBoolean(Companion.getDROPBOX_LOGOUT_PREF(), true);
        pm.removeValue(Companion.getDROPBOX_TOKEN_PREF());
        pm.removeValue(Companion.getDROPBOX_EMAIL_PREF());
        getViewState().setDropboxAccount(null);
    }
}