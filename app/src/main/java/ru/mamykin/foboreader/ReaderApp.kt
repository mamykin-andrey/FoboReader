package ru.mamykin.foboreader

import android.support.multidex.MultiDexApplication

import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.di.AppComponent
import ru.mamykin.foboreader.di.modules.AppModule
import ru.mamykin.foboreader.di.DaggerAppComponent
import ru.mamykin.foboreader.di.modules.PreferencesModule
import ru.mamykin.foboreader.data.storage.PreferencesManager

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore

import javax.inject.Inject

import io.fabric.sdk.android.Fabric
import ru.mamykin.foboreader.data.storage.PreferenceNames

class ReaderApp : MultiDexApplication(), PreferenceNames {
    @Inject
    lateinit var pm: PreferencesManager

    override fun onCreate() {
        super.onCreate()
        setupAppComponent()
        setupCrashlytics()
        setupTheme()
    }

    private fun setupCrashlytics() {
        // Отключем Crashlytics для Debug-сборок
        val core = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG).build()
        val crashlytics = Crashlytics.Builder()
                .core(core)
                .build()
        Fabric.with(this, crashlytics)
    }

    private fun setupTheme() {
        UiUtils.nightMode = pm!!.getBoolean(PreferenceNames.Companion.NIGHT_THEME_PREF)
    }

    private fun setupAppComponent() {
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .preferencesModule(PreferencesModule())
                .build()
        component.inject(this)
    }

    companion object {
        lateinit var component: AppComponent
            private set
    }
}