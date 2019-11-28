package ru.mamykin.core.di.component

import dagger.Component
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.di.module.AppModule
import ru.mamykin.core.di.module.DatabaseModule
import ru.mamykin.core.di.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class,
    ViewModelModule::class
])
interface AppComponent {
    fun settingsStorage(): SettingsStorage
}