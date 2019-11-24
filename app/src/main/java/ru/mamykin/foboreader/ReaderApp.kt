package ru.mamykin.foboreader

import androidx.multidex.MultiDexApplication
import ru.mamykin.core.di.ComponentHolder
import ru.mamykin.core.di.DependenciesProvider
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.foboreader.di.component.AppComponent
import ru.mamykin.foboreader.di.component.DaggerAppComponent
import ru.mamykin.foboreader.di.modules.AppModule
import javax.inject.Inject

class ReaderApp : MultiDexApplication(), ComponentHolder {

    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var settingsStorage: ru.mamykin.settings.data.SettingsStorage

    override fun dependenciesProvider(): DependenciesProvider = appComponent

    override fun onCreate() {
        super.onCreate()
        setupDagger()
        setupTheme()
    }

    private fun setupTheme() {
        val nightModeEnabled = settingsStorage.isNightTheme
        UiUtils.enableNightMode(nightModeEnabled)
    }

    private fun setupDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        appComponent.inject(this)
    }
}