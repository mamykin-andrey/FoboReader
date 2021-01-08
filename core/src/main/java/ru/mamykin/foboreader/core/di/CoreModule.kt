package ru.mamykin.foboreader.core.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.domain.usecase.GetVibrationEnabled
import ru.mamykin.foboreader.core.platform.VibratorHelper

val coreModule = module {
    single { PreferencesManager(get()) }
    single { AppSettingsStorage(get()) }
    factory { GetVibrationEnabled(get()) }
    single { VibratorHelper(androidContext(), get()) }
}