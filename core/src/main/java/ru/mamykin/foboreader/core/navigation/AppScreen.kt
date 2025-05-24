package ru.mamykin.foboreader.core.navigation

sealed class AppScreen(val route: String) {

    data object Main : AppScreen("main/{tabRoute}") {
        fun createRoute(tabRoute: String) = "main/$tabRoute"
    }

    data object BookDetails : AppScreen("details/{bookId}/{readAllowed}") {
        fun createRoute(bookId: Long, readAllowed: Boolean) = "details/$bookId/$readAllowed"
    }

    data object ReadBook : AppScreen("book/{bookId}") {
        fun createRoute(bookId: Long) = "book/$bookId"
    }

    data object ChooseColor : AppScreen("choose_color/{type}") {
        fun createRoute(type: CustomColorType) = "choose_color/$type"

        enum class CustomColorType {
            BACKGROUND,
            TEXT,
            TRANSLATION,
        }
    }

    data object ChooseAppLanguage : AppScreen("change_language")

    data object StoreBooks : AppScreen("store/{categoryId}") {
        fun createRoute(categoryId: String): String = "store/$categoryId"
    }

    data object StoreSearch : AppScreen("store_search")

    data object LearnNewWords : AppScreen("learn_new_words")

    data object AllWords : AppScreen("all_words")
}