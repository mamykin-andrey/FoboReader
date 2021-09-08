package ru.mamykin.foboreader.app.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.mamykin.foboreader.app.presentation.tabs.TabsFragment
import ru.mamykin.foboreader.book_details.presentation.BookDetailsFragment
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.my_books.presentation.MyBooksFragment
import ru.mamykin.foboreader.read_book.presentation.ReadBookFragment
import ru.mamykin.foboreader.settings.presentation.SettingsFragment
import ru.mamykin.foboreader.store.presentation.BooksStoreFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenProviderImpl @Inject constructor() : ScreenProvider {

    override fun readBookScreen(bookId: Long) = object : FragmentScreen {

        override fun createFragment(factory: FragmentFactory) = ReadBookFragment.newInstance(bookId)
    }

    override fun bookDetailsScreen(bookId: Long) = object : FragmentScreen {

        override fun createFragment(factory: FragmentFactory) = BookDetailsFragment.newInstance(bookId)
    }

    override fun booksStoreScreen() = object : FragmentScreen {

        override fun createFragment(factory: FragmentFactory) = BooksStoreFragment()
    }

    override fun myBookScreen() = object : FragmentScreen {

        override fun createFragment(factory: FragmentFactory): Fragment = MyBooksFragment()
    }

    override fun settingsScreen() = object : FragmentScreen {

        override fun createFragment(factory: FragmentFactory) = SettingsFragment()
    }

    override fun tabsScreen() = object : FragmentScreen {

        override fun createFragment(factory: FragmentFactory) = TabsFragment()
    }
}