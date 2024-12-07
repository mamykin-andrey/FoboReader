package ru.mamykin.foboreader.app.navigation

import androidx.compose.runtime.Composable
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import ru.mamykin.foboreader.my_books.list.MyBooksScreenNew
import javax.inject.Inject

class TabComposableProviderImpl @Inject constructor() : TabComposableProvider {

    @Composable
    override fun myBooksScreen(apiHolder: ApiHolder, commonApi: CommonApi) {
        MyBooksScreenNew(apiHolder, commonApi)
    }

    @Composable
    override fun booksStoreScreen() {
        // TODO
    }

    @Composable
    override fun settingsScreen() {
        // TODO
    }
}