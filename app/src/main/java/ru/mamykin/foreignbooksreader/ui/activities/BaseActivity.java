package ru.mamykin.foreignbooksreader.ui.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.arellomobile.mvp.MvpAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.mamykin.foreignbooksreader.R;
import ru.mamykin.foreignbooksreader.ReaderApp;
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager;
import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames;

public abstract class BaseActivity extends MvpAppCompatActivity implements PreferenceNames {
    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Inject
    PreferencesManager pm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());
        ButterKnife.bind(this);
        ReaderApp.getComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupBrightness();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupBrightness() {
        float brightness = pm.getBoolean(Companion.getBRIGHTNESS_AUTO_PREF(), true)
                ? getSystemBrightness() : pm.getFloat(Companion.getBRIGHTNESS_PREF(), 1f);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = brightness;
        getWindow().setAttributes(layoutParams);
    }

    private float getSystemBrightness() {
        try {
            return (float) Settings.System.getInt(
                    getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 1f;
    }

    protected void initToolbar(String title, boolean homeEnabled) {
        setSupportActionBar(toolbar);
        UiUtils.setTitle(this, title);
        UiUtils.setHomeEnabled(this, homeEnabled);
    }

    @LayoutRes
    public abstract int getLayout();
}