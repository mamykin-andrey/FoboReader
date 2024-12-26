package ru.mamykin.foboreader.app.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.mamykin.foboreader.book_details.details.BookDetailsUI
import ru.mamykin.foboreader.main.MainScreenUI
import ru.mamykin.foboreader.read_book.reader.ReadBookUI
import ru.mamykin.foboreader.store.list.BooksStoreListUI

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object BooksStoreList : Screen("store/{categoryId}") {
        fun createRoute(categoryId: String) = "store/$categoryId"
    }

    data object BookDetails : Screen("details/{bookId}") {
        fun createRoute(bookId: Long) = "details/$bookId"
    }

    data object ReadBook : Screen("book/{bookId}") {
        fun createRoute(bookId: Long) = "book/$bookId"
    }
}

@Composable
fun AppNavigation(onNightThemeSwitch: (Boolean) -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreenUI(
                onBookCategoryClick = { navController.navigate(Screen.BooksStoreList.createRoute(it)) },
                onBookDetailsClick = { navController.navigate(Screen.BookDetails.createRoute(it)) },
                onReadBookClick = { navController.navigate(Screen.ReadBook.createRoute(it)) },
                onNightThemeSwitch = onNightThemeSwitch,
            )
        }

        composable(
            route = Screen.BooksStoreList.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments!!.getString("categoryId")!!
            BooksStoreListUI(categoryId = categoryId, onBackClick = { navController.popBackStack() })
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments!!.getLong("bookId")
            BookDetailsUI(
                bookId,
                onBackPress = { navController.popBackStack() },
                onReadBookClick = { navController.navigate(Screen.ReadBook.createRoute(it)) },
            )
        }

        composable(
            route = Screen.ReadBook.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments!!.getLong("bookId")
            ReadBookUI(bookId)
        }
    }
}