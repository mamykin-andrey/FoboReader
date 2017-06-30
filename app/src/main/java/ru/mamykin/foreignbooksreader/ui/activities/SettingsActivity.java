package ru.mamykin.foreignbooksreader.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.presenters.SettingsPresenter;
import ru.mamykin.foreignbooksreader.views.SettingsView;

/**
 * Страница настроек
 */
public class SettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener,
        SettingsView {
    public static final String DROPBOX_DIALOG_TAG = "dropbox_dialog_tag";

    @BindView(R.id.switchBrightAuto)
    protected SwitchCompat switchBrightAuto;
    @BindView(R.id.switchNightTheme)
    protected SwitchCompat switchNightTheme;
    @BindView(R.id.seekbarBright)
    protected AppCompatSeekBar seekbarBright;
    @BindView(R.id.btnTranslateColor)
    protected View btnTranslateColor;
    @BindView(R.id.ivTranslateColor)
    protected ImageView ivTranslateColor;
    @BindView(R.id.btnDropboxLogout)
    protected View btnDropboxLogout;
    @BindView(R.id.btnTextSizeMinus)
    protected View btnTextSizeMinus;
    @BindView(R.id.btnTextSizePlus)
    protected View btnTextSizePlus;
    @BindView(R.id.tvTextSize)
    protected TextView tvTextSize;
    @BindView(R.id.tvDropboxAccount)
    protected TextView tvDropboxAccount;

    @InjectPresenter
    SettingsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar(getString(R.string.settings), true);
        seekbarBright.setOnSeekBarChangeListener(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        presenter.onBrightnessProgressChanged(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void setDropboxAccount(String account) {
        tvDropboxAccount.setText(account);
    }

    @Override
    public void showDropboxLogoutDialog() {
        UiUtils.showDialog(this, R.string.dropbox_logout_title, R.string.dropbox_logout_message,
                DROPBOX_DIALOG_TAG, () -> presenter.onDropboxLogoutPositive(), null);
    }

    @Override
    public void setContentSizeText(String text) {
        tvTextSize.setText(text);
    }

    @Override
    public void setBrightnessPos(int progress) {
        seekbarBright.setProgress(progress);
    }

    @Override
    public void setAutoBrightnessChecked(boolean enabled) {
        switchBrightAuto.setChecked(enabled);
    }

    @Override
    public void setBrightnessControlEnabled(boolean enabled) {
        seekbarBright.setEnabled(enabled);
    }

    @Override
    public void setNightThemeEnabled(boolean enabled) {
        switchNightTheme.setChecked(enabled);
    }

    @Override
    public void setTitle(String title) {
        UiUtils.setTitle(this, title);
    }

    @Override
    public void setHomeAsUpEnabled(boolean enabled) {
        UiUtils.setHomeEnabled(this, enabled);
    }

    @Override
    public void restartActivity() {
        UiUtils.restartActivity(this);
    }

    @OnCheckedChanged(R.id.switchNightTheme)
    public void onNightThemeCheckedChanged(boolean isChecked) {
        presenter.onNightThemeCheckedChanged(isChecked);
    }

    @OnCheckedChanged(R.id.switchBrightAuto)
    public void onBrightnessAutoCheckedChanged(boolean isChecked) {
        presenter.onBrightnessAutoCheckedChanged(isChecked);
    }

    @OnClick(R.id.btnDropboxLogout)
    public void onDropboxLogoutClicked() {
        presenter.onDropboxLogoutClick();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }
}