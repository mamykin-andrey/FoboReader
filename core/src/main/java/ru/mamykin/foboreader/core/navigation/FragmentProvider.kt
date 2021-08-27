package ru.mamykin.foboreader.core.navigation

import androidx.fragment.app.Fragment

interface FragmentProvider {

    fun readBookFragment(bookId: Long): Fragment

    fun bookDetailsFragment(): Fragment

    fun booksStoreFragment(): Fragment

    fun myBookFragment(): Fragment

    fun settingsFragment(): Fragment

    fun mainFragment(): Fragment
}