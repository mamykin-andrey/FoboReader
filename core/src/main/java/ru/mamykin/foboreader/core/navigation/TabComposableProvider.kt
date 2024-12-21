package ru.mamykin.foboreader.core.navigation

import androidx.compose.runtime.Composable

interface TabComposableProvider {
    @Composable
    fun MyBooksScreenTabContent()

    @Composable
    fun BooksStoreScreenTabContent()

    @Composable
    fun SettingsScreenTabContent()
}