package ru.mamykin.foboreader.app.presentation.tabs

import androidx.fragment.app.Fragment

sealed class Tab(
    val tag: String
) {
    private companion object Tags {
        private const val MY_BOOKS_FRAGMENT = "my_books_fragment"
        private const val BOOKS_STORE_FRAGMENT = "books_store_fragment"
        private const val SETTINGS_FRAGMENT = "settings_fragment"
    }

    abstract fun newFragment(): Fragment

    object MyBooks : Tab(MY_BOOKS_FRAGMENT) {

        override fun newFragment(): Fragment = MyBooksTabFragment()
    }

    object BooksStore : Tab(BOOKS_STORE_FRAGMENT) {

        override fun newFragment(): Fragment = BooksStoreTabFragment()
    }

    object Settings : Tab(SETTINGS_FRAGMENT) {

        override fun newFragment(): Fragment = SettingsTabFragment()
    }
}