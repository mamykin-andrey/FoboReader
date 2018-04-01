package ru.mamykin.foboreader

import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import io.fabric.sdk.android.Fabric
import ru.mamykin.foboreader.data.repository.settings.SettingsStorage
import ru.mamykin.foboreader.di.component.AppComponent
import ru.mamykin.foboreader.di.component.DaggerAppComponent
import ru.mamykin.foboreader.di.modules.AppModule
import ru.mamykin.foboreader.ui.global.UiUtils
import javax.inject.Inject

class ReaderApp : MultiDexApplication() {

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }

    @Inject
    lateinit var settingsStorage: SettingsStorage

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
        val nightModeEnabled = settingsStorage.nightThemeEnabled
        UiUtils.enableNightMode(nightModeEnabled)
    }

    private fun setupDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        appComponent.inject(this)
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }
}