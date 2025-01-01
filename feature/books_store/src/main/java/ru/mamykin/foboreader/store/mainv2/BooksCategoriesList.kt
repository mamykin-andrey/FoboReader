package ru.mamykin.foboreader.store.mainv2

internal sealed class BooksStoreScreen(val route: String) {
    data object BooksStoreList : BooksStoreScreen("store/{categoryId}") {
        fun createRoute(categoryId: String) = "store/$categoryId"
    }

    data object BooksCategoriesList : BooksStoreScreen("store") {
        fun createRoute() = "store"
    }
}