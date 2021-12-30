package ru.mamykin.foboreader.main

sealed class Tab {
    companion object {
        const val MY_BOOKS_FRAGMENT = "my_books_fragment"
        const val BOOKS_STORE_FRAGMENT = "books_store_fragment"
        const val SETTINGS_FRAGMENT = "settings_fragment"
    }

    abstract val tag: String

    object MyBooks : Tab() {
        override val tag: String = MY_BOOKS_FRAGMENT
    }

    object BooksStore : Tab() {
        override val tag: String = BOOKS_STORE_FRAGMENT
    }

    object Settings : Tab() {
        override val tag: String = SETTINGS_FRAGMENT

    }
}