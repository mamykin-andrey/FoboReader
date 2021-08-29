package ru.mamykin.foboreader.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.mamykin.foboreader.app.navigation.ScreenProviderImpl
import ru.mamykin.foboreader.app.presentation.TabsViewModel
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.domain.usecase.GetVibrationEnabled
import ru.mamykin.foboreader.core.navigation.LocalCiceroneHolder
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.platform.VibratorHelper

val appModule = module {
    viewModel { TabsViewModel() }
    single { ScreenProviderImpl() }.bind(ScreenProvider::class)
    single { LocalCiceroneHolder() }.bind(LocalCiceroneHolder::class)
    single { PreferencesManager(get()) }
    single { AppSettingsStorage(get()) }
    factory { GetVibrationEnabled(get()) }
    single { VibratorHelper(androidContext(), get()) }
}