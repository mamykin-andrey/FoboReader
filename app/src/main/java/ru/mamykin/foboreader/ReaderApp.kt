package ru.mamykin.foboreader

import androidx.multidex.MultiDexApplication
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.di.ComponentHolder
import ru.mamykin.core.di.component.AppComponent
import ru.mamykin.core.di.component.DaggerAppComponent
import ru.mamykin.core.di.module.AppModule
import ru.mamykin.core.ui.UiUtils

class ReaderApp : MultiDexApplication(), ComponentHolder {

    private lateinit var appComponent: AppComponent

    private lateinit var settingsStorage: SettingsStorage

    override fun onCreate() {
        super.onCreate()
        setupDagger()
        setupTheme()
    }

    override fun getAppComponent(): AppComponent = appComponent

    private fun setupTheme() {
        val nightModeEnabled = settingsStorage.isNightTheme
        UiUtils.enableNightMode(nightModeEnabled)
    }

    private fun setupDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        settingsStorage = appComponent.settingsStorage()
    }
}