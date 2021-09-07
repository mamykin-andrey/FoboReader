package ru.mamykin.foboreader.app.di

import dagger.Component
import ru.mamykin.foboreader.app.presentation.MainActivity
import ru.mamykin.foboreader.app.presentation.tabs.BooksStoreTabFragment
import ru.mamykin.foboreader.app.presentation.tabs.MyBooksTabFragment
import ru.mamykin.foboreader.app.presentation.tabs.SettingsTabFragment
import ru.mamykin.foboreader.core.di.api.NavigationApi

@MainScope
@Component(dependencies = [NavigationApi::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    fun inject(myBooksTabFragment: MyBooksTabFragment)

    fun inject(booksStoreTabFragment: BooksStoreTabFragment)

    fun inject(settingsTabFragment: SettingsTabFragment)

    @Component.Factory
    interface Factory {

        fun create(
            navigationApi: NavigationApi
        ): MainComponent
    }
}