package ru.mamykin.foboreader.app.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.mamykin.foboreader.book_details.details.BookDetailsUI
import ru.mamykin.foboreader.main.BottomNavigationTabRoute
import ru.mamykin.foboreader.main.MainScreenUI
import ru.mamykin.foboreader.my_books.list.MyBooksScreen
import ru.mamykin.foboreader.read_book.reader.ReadBookUI
import ru.mamykin.foboreader.settings.all_settings.SettingsTabUI
import ru.mamykin.foboreader.settings.app_language.ChooseAppLanguageDialogUI
import ru.mamykin.foboreader.settings.common.CustomColorType
import ru.mamykin.foboreader.settings.custom_color.ChooseCustomColorDialogUI
import ru.mamykin.foboreader.store.mainv2.BooksStoreUI

sealed class Screen(val route: String) {
    data object Main : Screen("main/{tabRoute}") {
        fun createRoute(tabRoute: String) = "main/$tabRoute"
    }

    data object BookDetails : Screen("details/{bookId}") {
        fun createRoute(bookId: Long) = "details/$bookId"
    }

    data object ReadBook : Screen("book/{bookId}") {
        fun createRoute(bookId: Long) = "book/$bookId"
    }

    data object ChooseCustomColor : Screen("choose_color/{type}") {
        fun createRoute(type: CustomColorType) = "choose_color/$type"
    }

    data object ChooseAppLanguage : Screen("change_language")
}

@Composable
fun AppNavigation(onNightThemeSwitch: (Boolean) -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Main.createRoute(BottomNavigationTabRoute.MY_BOOKS),
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        composable(
            route = Screen.Main.route,
            arguments = listOf(navArgument("tabRoute") { type = NavType.StringType })
        ) { backStackEntry ->
            val tabRoute = backStackEntry.arguments!!.getString("tabRoute")!!
            MainScreenUI(
                selectedTabRoute = tabRoute,
                navigationTabs = listOf(
                    BottomNavigationTabRoute.MY_BOOKS to {
                        MyBooksScreen(
                            onBookDetailsClick = { navController.navigate(Screen.BookDetails.createRoute(it)) },
                            onReadBookClick = { navController.navigate(Screen.ReadBook.createRoute(it)) },
                        )
                    },
                    BottomNavigationTabRoute.BOOKS_STORE to {
                        BooksStoreUI()
                    },
                    BottomNavigationTabRoute.SETTINGS to {
                        SettingsTabUI(
                            navController = navController,
                            onNightThemeSwitch = onNightThemeSwitch,
                            onChooseColorClick = { navController.navigate(Screen.ChooseCustomColor.createRoute(it)) },
                            onChooseAppLanguageClick = { navController.navigate(Screen.ChooseAppLanguage.route) },
                        )
                    }
                )
            )
        }

        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) {
            BookDetailsUI(
                onBackPress = { navController.popBackStack() },
            ) { navController.navigate(Screen.ReadBook.createRoute(it)) }
        }

        composable(
            route = Screen.ReadBook.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) {
            ReadBookUI(onBackPress = { navController.popBackStack() })
        }

        dialog(
            route = Screen.ChooseCustomColor.route,
            arguments = listOf(
                navArgument("type") { type = NavType.EnumType(CustomColorType::class.java) },
            )
        ) {
            ChooseCustomColorDialogUI(navController)
        }

        dialog(route = Screen.ChooseAppLanguage.route) {
            ChooseAppLanguageDialogUI(navController)
        }
    }
}