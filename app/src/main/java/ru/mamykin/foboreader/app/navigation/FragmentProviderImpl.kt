package ru.mamykin.foboreader.app.navigation

import ru.mamykin.foboreader.app.presentation.TabsFragment
import ru.mamykin.foboreader.book_details.presentation.BookDetailsFragment
import ru.mamykin.foboreader.core.navigation.FragmentProvider
import ru.mamykin.foboreader.my_books.presentation.MyBooksFragment
import ru.mamykin.foboreader.read_book.presentation.ReadBookFragment
import ru.mamykin.foboreader.settings.presentation.SettingsFragment
import ru.mamykin.foboreader.store.presentation.BooksStoreFragment

class FragmentProviderImpl : FragmentProvider {

    override fun readBookFragment(bookId: Long) = ReadBookFragment.newInstance(bookId)

    override fun bookDetailsFragment() = BookDetailsFragment()

    override fun booksStoreFragment() = BooksStoreFragment()

    override fun myBookFragment() = MyBooksFragment()

    override fun settingsFragment() = SettingsFragment()

    override fun tabsFragment() = TabsFragment()
}