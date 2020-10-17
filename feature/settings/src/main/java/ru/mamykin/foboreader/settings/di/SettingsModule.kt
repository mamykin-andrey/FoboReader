package ru.mamykin.foboreader.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.settings.domain.interactor.SettingsInteractor
import ru.mamykin.foboreader.settings.navigation.LocalSettingsNavigator
import ru.mamykin.foboreader.settings.presentation.SettingsViewModel

val settingsModule = module {
    single { LocalSettingsNavigator() }
    factory { SettingsInteractor(get()) }
    viewModel { SettingsViewModel(get(), get()) }
}