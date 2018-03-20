package ru.mamykin.foreignbooksreader;

import android.support.multidex.MultiDexApplication;

import ru.mamykin.foreignbooksreader.common.UiUtils;
import ru.mamykin.foreignbooksreader.di.AppComponent;
import ru.mamykin.foreignbooksreader.di.modules.AppModule;
import ru.mamykin.foreignbooksreader.di.DaggerAppComponent;
import ru.mamykin.foreignbooksreader.di.modules.PreferencesModule;
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import ru.mamykin.foreignbooksreader.preferences.PreferenceNames;

public class ReaderApp extends MultiDexApplication implements PreferenceNames {
    private static AppComponent component;
    @Inject
    protected PreferencesManager pm;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupAppComponent();
        setupCrashlytics();
        setupTheme();
    }

    private void setupCrashlytics() {
        // Отключем Crashlytics для Debug-сборок
        CrashlyticsCore core = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG).build();
        Crashlytics crashlytics = new Crashlytics.Builder()
                .core(core)
                .build();
        Fabric.with(this, crashlytics);
    }

    private void setupTheme() {
        UiUtils.setNightMode(pm.getBoolean(Companion.getNIGHT_THEME_PREF()));
    }

    private void setupAppComponent() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .preferencesModule(new PreferencesModule())
                .build();
        component.inject(this);
    }
}