package ru.mamykin.foboreader

import androidx.multidex.MultiDexApplication
import ru.mamykin.foboreader.core.di.component.AppComponent
import ru.mamykin.foboreader.core.di.component.DaggerAppComponent
import ru.mamykin.foboreader.core.di.modules.AppModule
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.data.repository.settings.SettingsStorage
import javax.inject.Inject

class ReaderApp : MultiDexApplication() {

    lateinit var appComponent: AppComponent
        private set

    @Inject
    lateinit var settingsStorage: SettingsStorage

    override fun onCreate() {
        super.onCreate()
        setupDagger()
        setupTheme()
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
}