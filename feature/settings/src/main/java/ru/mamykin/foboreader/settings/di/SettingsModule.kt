package ru.mamykin.foboreader.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.settings.domain.usecase.*
import ru.mamykin.foboreader.settings.presentation.SettingsViewModel

val settingsModule = module {
    factory { GetSettings(get()) }
    factory { SetBrightness(get()) }
    factory { SetNightTheme(get()) }
    factory { SetTextSize(get()) }
    factory { SetAppLanguage(get()) }
    factory { GetTranslationColors(get()) }
    factory { SetTranslationColor(get()) }
    factory { SetUseVibration(get()) }
    factory { GetAppLanguages() }
    factory { GetSelectedLanguage(get()) }
    viewModel { SettingsViewModel(get(), get(), get(), get(), get()) }
}