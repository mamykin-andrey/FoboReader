package ru.mamykin.foboreader.app.presentation

import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.my_books.presentation.MyBooksFragment
import ru.mamykin.foboreader.settings.presentation.SettingsFragment
import ru.mamykin.foboreader.store.presentation.BooksStoreFragment

sealed class Tab(
    val tag: String
) {
    private companion object Tags {
        private const val MY_BOOKS_FRAGMENT = "home_tab_fragment"
        private const val BOOKS_STORE_FRAGMENT = "dashboard_tab_fragment"
        private const val SETTINGS_FRAGMENT = "notifications_tab_fragment"
    }

    abstract fun newFragment(): Fragment

    object MyBooksTab : Tab(MY_BOOKS_FRAGMENT) {

        override fun newFragment(): Fragment = MyBooksFragment()
    }

    object BooksStoreTab : Tab(BOOKS_STORE_FRAGMENT) {

        override fun newFragment(): Fragment = BooksStoreFragment()
    }

    object SettingsTab : Tab(SETTINGS_FRAGMENT) {

        override fun newFragment(): Fragment = SettingsFragment()
    }
}