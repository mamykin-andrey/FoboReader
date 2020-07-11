package ru.mamykin.foboreader.core.di

import org.koin.dsl.module
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.data.storage.SettingsStorage

val coreModule = module {
    single { PreferencesManager(get()) }
    single { SettingsStorage(get()) }
}