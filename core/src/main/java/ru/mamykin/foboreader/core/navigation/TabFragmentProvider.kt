package ru.mamykin.foboreader.core.navigation

import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment

interface TabFragmentProvider {
    fun newMyBooksFragment(): Fragment
    fun newBooksStoreFragment(): Fragment
    fun newSettingsFragment(): Fragment

    // @Composable
    // fun myBooksScreen()
    //
    // @Composable
    // fun booksStoreScreen()
    //
    // @Composable
    // fun settingsFragment()
}