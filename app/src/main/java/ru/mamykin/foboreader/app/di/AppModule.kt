package ru.mamykin.foboreader.app.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.mamykin.foboreader.app.navigation.FragmentProviderImpl
import ru.mamykin.foboreader.app.presentation.TabsViewModel
import ru.mamykin.foboreader.core.navigation.FragmentProvider
import ru.mamykin.foboreader.core.navigation.LocalCiceroneHolder

val appModule = module {
    viewModel { TabsViewModel() }
    single { FragmentProviderImpl() }.bind(FragmentProvider::class)
    single { LocalCiceroneHolder() }.bind(LocalCiceroneHolder::class)
}