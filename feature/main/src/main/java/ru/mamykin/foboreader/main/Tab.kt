package ru.mamykin.foboreader.main

import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.navigation.TabFragmentProvider

sealed class Tab {
    companion object {
        const val MY_BOOKS_FRAGMENT = "my_books_fragment"
        const val BOOKS_STORE_FRAGMENT = "books_store_fragment"
        const val SETTINGS_FRAGMENT = "settings_fragment"
    }

    abstract val tag: String

    abstract fun newFragment(provider: TabFragmentProvider): Fragment

    object MyBooks : Tab() {
        override val tag: String = MY_BOOKS_FRAGMENT

        override fun newFragment(provider: TabFragmentProvider): Fragment = provider.newMyBooksFragment()
    }

    object BooksStore : Tab() {
        override val tag: String = BOOKS_STORE_FRAGMENT

        override fun newFragment(provider: TabFragmentProvider): Fragment = provider.newBooksStoreFragment()
    }

    object Settings : Tab() {
        override val tag: String = SETTINGS_FRAGMENT

        override fun newFragment(provider: TabFragmentProvider): Fragment = provider.newSettingsFragment()
    }
}