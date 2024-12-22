package ru.mamykin.foboreader.app.navigation

import androidx.compose.runtime.Composable
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import ru.mamykin.foboreader.my_books.list.MyBooksScreen
import ru.mamykin.foboreader.settings.all_settings.SettingsTabUI
import ru.mamykin.foboreader.store.main.BooksCategoriesScreen
import javax.inject.Inject

class TabComposableProviderImpl @Inject constructor() : TabComposableProvider {

    @Composable
    override fun MyBooksScreenTabContent() {
        MyBooksScreen()
    }

    @Composable
    override fun BooksStoreScreenTabContent() {
        BooksCategoriesScreen()
    }

    @Composable
    override fun SettingsScreenTabContent() {
        SettingsTabUI()
    }
}