package ru.mamykin.foboreader.app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.mamykin.foboreader.app.navigation.AppNavigator
import ru.mamykin.foboreader.app.navigation.featureNavigators
import ru.mamykin.foboreader.app.presentation.TabsViewModel

val appModule = module {
    single { AppNavigator() }.binds(featureNavigators)
    viewModel { TabsViewModel() }
}