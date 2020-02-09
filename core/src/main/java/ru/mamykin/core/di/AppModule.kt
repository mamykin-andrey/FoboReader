package ru.mamykin.core.di

import org.koin.dsl.module
import ru.mamykin.core.data.PreferencesManager
import ru.mamykin.core.data.SettingsStorage

val coreModule = module {
    single { PreferencesManager(get()) }
    single { SettingsStorage(get()) }
}