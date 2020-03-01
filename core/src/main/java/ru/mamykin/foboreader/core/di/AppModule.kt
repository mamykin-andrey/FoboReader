package ru.mamykin.foboreader.core.di

import org.koin.dsl.module
import ru.mamykin.foboreader.core.data.PreferencesManager
import ru.mamykin.foboreader.core.data.SettingsStorage

val coreModule = module {
    single { PreferencesManager(get()) }
    single { SettingsStorage(get()) }
}