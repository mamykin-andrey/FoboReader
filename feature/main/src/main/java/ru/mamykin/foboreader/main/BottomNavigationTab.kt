package ru.mamykin.foboreader.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationTab(
    val route: String,
    val tabName: String,
    val icon: ImageVector,
) {
    data object MyBooks : BottomNavigationTab(
        "my_books",
        "My books",
        Icons.Filled.Book,
    )

    data object BooksStore : BottomNavigationTab(
        "books_store",
        "Books store",
        Icons.Filled.Storefront,
    )

    data object Settings : BottomNavigationTab(
        "settings",
        "Settings",
        Icons.Filled.Settings,
    )
}