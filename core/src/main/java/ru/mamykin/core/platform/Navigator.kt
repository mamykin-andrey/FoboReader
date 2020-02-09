package ru.mamykin.core.platform

interface Navigator {

    fun openMyBooksScreen()

    fun openTabsScreen()

    fun openBook(id: Long)

    fun openBookDetails(id: Long)
}