package ru.mamykin.foboreader.app.navigation

import androidx.compose.runtime.Composable
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import javax.inject.Inject

class TabComposableProviderImpl @Inject constructor() : TabComposableProvider {

    @Composable
    override fun MyBooksScreen(apiHolder: ApiHolder, commonApi: CommonApi) {
        ru.mamykin.foboreader.my_books.list.MyBooksScreen(apiHolder, commonApi)
    }

    @Composable
    override fun BooksStoreScreen() {
        // TODO
    }

    @Composable
    override fun SettingsScreen() {
        // TODO
    }
}