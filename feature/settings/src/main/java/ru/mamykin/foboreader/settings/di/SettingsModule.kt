package ru.mamykin.foboreader.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.settings.presentation.SettingsViewModel

val settingsModule = module {
    viewModel { SettingsViewModel(get()) }
}