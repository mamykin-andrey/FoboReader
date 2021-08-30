package ru.mamykin.foboreader.app.di

import dagger.Subcomponent
import ru.mamykin.foboreader.app.presentation.MainActivity
import ru.mamykin.foboreader.app.presentation.tabs.BooksStoreTabFragment
import ru.mamykin.foboreader.app.presentation.tabs.MyBooksTabFragment
import ru.mamykin.foboreader.app.presentation.tabs.SettingsTabFragment
import ru.mamykin.foboreader.app.presentation.tabs.TabsFragment

@Subcomponent
interface MainComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: TabsFragment)

    fun inject(myBooksTabFragment: MyBooksTabFragment)

    fun inject(booksStoreTabFragment: BooksStoreTabFragment)

    fun inject(settingsTabFragment: SettingsTabFragment)
}