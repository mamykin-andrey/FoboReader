package ru.mamykin.foboreader.core.navigation

import androidx.compose.runtime.Composable

interface TabComposableProvider {
    @Composable
    fun MyBooksScreenTabContent(onBookDetailsClick: (Long) -> Unit, onReadBookClick: (Long) -> Unit)

    @Composable
    fun BooksStoreScreenTabContent(onBookCategoryClick: (String) -> Unit)

    @Composable
    fun SettingsScreenTabContent()
}