package ru.mamykin.foboreader.core.navigation

import androidx.fragment.app.Fragment

interface TabFragmentProvider {
    fun newMyBooksFragment(): Fragment
    fun newBooksStoreFragment(): Fragment
    fun newSettingsFragment(): Fragment
}