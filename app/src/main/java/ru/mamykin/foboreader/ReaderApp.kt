package ru.mamykin.foboreader

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.data.storage.PreferenceNames
import ru.mamykin.foboreader.data.storage.PreferencesManager
import ru.mamykin.foboreader.di.AppComponent
import ru.mamykin.foboreader.di.modules.AppModule
import ru.mamykin.foboreader.di.modules.PreferencesModule
import javax.inject.Inject

class ReaderApp : MultiDexApplication(), PreferenceNames {

    companion object {
        lateinit var component: AppComponent
            private set
    }

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate() {
        super.onCreate()
        setupDagger()
        setupCrashlytics()
        setupTheme()
    }

    private fun setupCrashlytics() {
        val core = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()

        val crashlytics = Crashlytics.Builder()
                .core(core)
                .build()

        Fabric.with(this, crashlytics)
    }

    private fun setupTheme() {
        UiUtils.nightMode = preferencesManager.getBoolean(PreferenceNames.NIGHT_THEME_PREF)
    }

    private fun setupDagger() {
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .preferencesModule(PreferencesModule())
                .build()

        component.inject(this)
    }
}