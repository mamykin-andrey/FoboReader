package ru.mamykin.foboreader.core.di

import org.koin.dsl.module
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage

val coreModule = module {
    single { PreferencesManager(get()) }
    single { AppSettingsStorage(get()) }
}