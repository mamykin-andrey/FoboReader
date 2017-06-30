package ru.mamykin.foreignbooksreader.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends MvpView {
    void setDropboxAccount(String account);

    void showDropboxLogoutDialog();

    void setContentSizeText(String text);

    void setBrightnessPos(int position);

    void setAutoBrightnessChecked(boolean enabled);

    void setBrightnessControlEnabled(boolean enabled);

    void setNightThemeEnabled(boolean enabled);

    void setTitle(String title);

    void setHomeAsUpEnabled(boolean enabled);

    void restartActivity();

    void setupBrightness();
}