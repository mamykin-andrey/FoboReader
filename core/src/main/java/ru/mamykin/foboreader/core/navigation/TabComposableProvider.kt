package ru.mamykin.foboreader.core.navigation

import androidx.compose.runtime.Composable
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.CommonApi

interface TabComposableProvider {
    @Composable
    fun MyBooksScreen(apiHolder: ApiHolder, commonApi: CommonApi)

    @Composable
    fun BooksStoreScreen()

    @Composable
    fun SettingsScreen()
}