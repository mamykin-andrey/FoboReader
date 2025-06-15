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
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.core.navigation.MainTabScreenRoutes
import ru.mamykin.foboreader.dictionary_impl.DictionaryUI
import ru.mamykin.foboreader.learn_new_words.LearnNewWordsUI
import ru.mamykin.foboreader.main.MainScreenUI
import ru.mamykin.foboreader.my_books.list.MyBooksScreen
import ru.mamykin.foboreader.read_book.reader.ReadBookUI
import ru.mamykin.foboreader.settings.all_settings.SettingsTabUI
import ru.mamykin.foboreader.settings.app_language.ChooseAppLanguageDialogUI
import ru.mamykin.foboreader.settings.custom_color.ChooseCustomColorDialogUI
import ru.mamykin.foboreader.store.categories.StoreMainUI
import ru.mamykin.foboreader.store.list.StoreBooksUI
import ru.mamykin.foboreader.store.search.StoreSearchUI
import ru.mamykin.foboreader.onboarding.OnboardingScreen

@Composable
fun AppNavigation(
    onNightThemeSwitch: (Boolean) -> Unit,
    isOnboardingCompleted: Boolean
) {
    val navController = rememberNavController()
    val startDestination = if (isOnboardingCompleted) {
        AppScreen.Main.createRoute(MainTabScreenRoutes.MY_BOOKS)
    } else {
        AppScreen.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = AppScreen.Onboarding.route) {
            OnboardingScreen(navController)
        }

        composable(
            route = AppScreen.Main.route,
            arguments = listOf(navArgument("tabRoute") { type = NavType.StringType })
        ) { backStackEntry ->
            val tabRoute = backStackEntry.arguments!!.getString("tabRoute")!!
            MainScreenUI(
                selectedTabRoute = tabRoute,
                navigationTabs = listOf(
                    MainTabScreenRoutes.MY_BOOKS to {
                        MyBooksScreen(navController)
                    },
                    MainTabScreenRoutes.MY_WORDS to {
                        DictionaryUI(navController)
                    },
                    MainTabScreenRoutes.BOOKS_STORE to {
                        StoreMainUI(navController)
                    },
                    MainTabScreenRoutes.SETTINGS to {
                        SettingsTabUI(
                            appNavController = navController,
                            onNightThemeSwitch = onNightThemeSwitch,
                        )
                    }
                )
            )
        }

        composable(
            route = AppScreen.BookDetails.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.LongType },
                navArgument("readAllowed") { type = NavType.BoolType },
            )
        ) {
            BookDetailsUI(navController)
        }

        composable(
            route = AppScreen.ReadBook.route,
            arguments = listOf(navArgument("bookId") { type = NavType.LongType })
        ) {
            ReadBookUI(navController)
        }

        composable(
            route = AppScreen.StoreBooks.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
            )
        ) {
            StoreBooksUI(navController)
        }

        composable(
            route = AppScreen.StoreSearch.route
        ) {
            StoreSearchUI(navController)
        }

        composable(
            route = AppScreen.LearnNewWords.route,
        ) {
            LearnNewWordsUI(navController)
        }

        dialog(
            route = AppScreen.ChooseColor.route,
            arguments = listOf(
                navArgument("type") { type = NavType.EnumType(AppScreen.ChooseColor.CustomColorType::class.java) },
            )
        ) {
            ChooseCustomColorDialogUI(navController)
        }

        dialog(route = AppScreen.ChooseAppLanguage.route) {
            ChooseAppLanguageDialogUI(navController)
        }
    }
}