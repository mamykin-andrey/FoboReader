package ru.mamykin.foboreader.app.navigation

import androidx.compose.runtime.Composable
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import ru.mamykin.foboreader.my_books.list.MyBooksScreen
import ru.mamykin.foboreader.settings.all_settings.SettingsTabUI
import ru.mamykin.foboreader.store.main.BooksCategoriesScreen
import javax.inject.Inject

class TabComposableProviderImpl @Inject constructor(
    private val navigationApi: NavigationApi,
    private val commonApi: CommonApi,
    private val networkApi: NetworkApi,
    private val settingsApi: SettingsApi,
) : TabComposableProvider {

    @Composable
    override fun MyBooksScreenTabContent() {
        MyBooksScreen(navigationApi, commonApi)
    }

    @Composable
    override fun BooksStoreScreenTabContent() {
        BooksCategoriesScreen(commonApi, networkApi, navigationApi, settingsApi)
    }

    @Composable
    override fun SettingsScreenTabContent() {
        SettingsTabUI(navigationApi, commonApi, settingsApi)
    }
}