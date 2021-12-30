package ru.mamykin.foboreader.app.navigation

import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.my_books.presentation.MyBooksFragment
import ru.mamykin.foboreader.settings.presentation.SettingsFragment
import ru.mamykin.foboreader.store.presentation.BooksStoreFragment
import ru.mamykin.foboreader.core.navigation.TabFragmentProvider
import javax.inject.Inject

class TabFragmentProviderImpl @Inject constructor() : TabFragmentProvider {

    override fun newMyBooksFragment(): Fragment = MyBooksFragment()

    override fun newBooksStoreFragment(): Fragment = BooksStoreFragment()

    override fun newSettingsFragment(): Fragment = SettingsFragment()
}