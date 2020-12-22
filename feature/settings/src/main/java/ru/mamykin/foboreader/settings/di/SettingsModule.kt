package ru.mamykin.foboreader.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.settings.domain.usecase.GetSettings
import ru.mamykin.foboreader.settings.domain.usecase.SetBrightness
import ru.mamykin.foboreader.settings.domain.usecase.SetNightTheme
import ru.mamykin.foboreader.settings.domain.usecase.SetTextSize
import ru.mamykin.foboreader.settings.navigation.LocalSettingsNavigator
import ru.mamykin.foboreader.settings.presentation.SettingsViewModel

val settingsModule = module {
    single { LocalSettingsNavigator() }
    factory { GetSettings(get()) }
    factory { SetBrightness(get()) }
    factory { SetNightTheme(get()) }
    factory { SetTextSize(get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get(), get()) }
}