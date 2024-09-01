package ru.mamykin.foboreader.app.navigation

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.mamykin.foboreader.main.MainFragment
import ru.mamykin.foboreader.book_details.details.BookDetailsFragment
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.read_book.reader.ReadBookFragment
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

    override fun mainScreen() = object : FragmentScreen {

        override fun createFragment(factory: FragmentFactory) = MainFragment()
    }
}