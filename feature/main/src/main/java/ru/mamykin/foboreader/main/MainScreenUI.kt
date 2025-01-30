package ru.mamykin.foboreader.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.core.navigation.MainTabScreenRoutes
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme

@Composable
fun MainScreenUI(
    selectedTabRoute: String,
    navigationTabs: List<Pair<String, @Composable () -> Unit>>,
) {
    val viewModel: MainViewModel = hiltViewModel()
    val navController = rememberNavController()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MainScreenComposable(
        state = state,
        navigationTabs = navigationTabs,
        navController = navController,
        selectedTabRoute = selectedTabRoute,
    )
}

private fun navigateToScreen(
    navController: NavHostController,
    route: String,
) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun MainScreenComposable(
    state: MainViewModel.State,
    navigationTabs: List<Pair<String, @Composable () -> Unit>>,
    navController: NavHostController,
    selectedTabRoute: String,
) {
    Scaffold(
        bottomBar = {
            NavigationBarComposable(
                state = state,
                navController = navController,
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = selectedTabRoute,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            navigationTabs.forEach { (route, composable) ->
                composable(route) {
                    composable()
                }
            }
        }
    }
}

@Composable
private fun NavigationBarComposable(
    state: MainViewModel.State,
    navController: NavHostController,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        state.tabs.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(item.tabNameId)) },
                label = { Text(stringResource(item.tabNameId)) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navigateToScreen(navController, item.route)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    FoboReaderTheme {
        MainScreenComposable(
            state = MainViewModel.State(),
            navController = rememberNavController(),
            navigationTabs = listOf(
                MainTabScreenRoutes.MY_BOOKS to {
                    Text("My books")
                },
                MainTabScreenRoutes.MY_WORDS to {
                    Text("My words")
                },
                MainTabScreenRoutes.BOOKS_STORE to {
                    Text("Books store")
                },
                MainTabScreenRoutes.SETTINGS to {
                    Text("Settings")
                }
            ),
            selectedTabRoute = MainTabScreenRoutes.MY_BOOKS,
        )
    }
}