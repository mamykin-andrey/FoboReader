package ru.mamykin.foboreader.app.navigation

import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.navigation.TabFragmentProvider
import ru.mamykin.foboreader.my_books.list.MyBooksFragment
import ru.mamykin.foboreader.settings.all_settings.SettingsFragment
import ru.mamykin.foboreader.store.categories.BookCategoriesFragment
import javax.inject.Inject

class TabFragmentProviderImpl @Inject constructor() : TabFragmentProvider {

    override fun newMyBooksFragment(): Fragment = MyBooksFragment.newInstance()

    override fun newBooksStoreFragment(): Fragment = BookCategoriesFragment.newInstance()

    override fun newSettingsFragment(): Fragment = SettingsFragment.newInstance()

    // override fun myBooksScreen() {
    //     TODO("Not yet implemented")
    // }
    //
    // override fun booksStoreScreen() {
    //     TODO("Not yet implemented")
    // }
    //
    // override fun settingsFragment() {
    //     TODO("Not yet implemented")
    // }
}