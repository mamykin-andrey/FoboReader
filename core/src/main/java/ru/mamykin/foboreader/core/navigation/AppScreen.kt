package ru.mamykin.foboreader.core.navigation

sealed class AppScreen(val route: String) {

    data object Main : AppScreen("main/{tabRoute}") {
        fun createRoute(tabRoute: String) = "main/$tabRoute"
    }

    data object BookDetails : AppScreen("details/{bookId}") {
        fun createRoute(bookId: Long) = "details/$bookId"
    }

    data object ReadBook : AppScreen("book/{bookId}") {
        fun createRoute(bookId: Long) = "book/$bookId"
    }

    data object ChooseCustomColor : AppScreen("choose_color/{type}") {
        fun createRoute(type: String) = "choose_color/$type"
    }

    data object ChooseAppLanguage : AppScreen("change_language")

    data object StoreBooks : AppScreen("store/{categoryId}") {
        fun createRoute(categoryId: String): String = "store/$categoryId"
    }

    data object StoreSearch : AppScreen("store_search")
}