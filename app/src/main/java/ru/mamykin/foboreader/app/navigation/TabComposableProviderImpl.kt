package ru.mamykin.foboreader.app.navigation

import androidx.compose.runtime.Composable
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import ru.mamykin.foboreader.my_books.list.MyBooksScreen
import ru.mamykin.foboreader.settings.all_settings.SettingsTabUI
import ru.mamykin.foboreader.store.main.BooksCategoriesScreen
import javax.inject.Inject

class TabComposableProviderImpl @Inject constructor() : TabComposableProvider {

    @Composable
    override fun MyBooksScreenTabContent(onBookDetailsClick: (Long) -> Unit, onReadBookClick: (Long) -> Unit) {
        MyBooksScreen(onBookDetailsClick, onReadBookClick)
    }

    @Composable
    override fun BooksStoreScreenTabContent(onBookCategoryClick: (String) -> Unit) {
        BooksCategoriesScreen(onBookCategoryClick)
    }

    @Composable
    override fun SettingsScreenTabContent(onNightThemeSwitch: (Boolean) -> Unit) {
        SettingsTabUI(onNightThemeSwitch)
    }
}