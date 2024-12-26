package ru.mamykin.foboreader.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme

@Composable
fun MainScreenUI(
    selectedTabRoute: String,
    navigationTabs: List<Pair<String, @Composable () -> Unit>>,
) {
    val viewModel: MainViewModel = hiltViewModel()
    val navController = rememberNavController()
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, navController = navController)
        }
    }
    MainScreenComposable(
        state = viewModel.state,
        onIntent = viewModel::sendIntent,
        navigationTabs = navigationTabs,
        navController = navController,
        selectedTabRoute = selectedTabRoute,
    )
}

private fun takeEffect(effect: MainViewModel.Effect, navController: NavHostController) = when (effect) {
    is MainViewModel.Effect.NavigateToTab -> {
        navigateToScreen(navController, effect.route)
    }
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
    onIntent: (MainViewModel.Intent) -> Unit,
    navigationTabs: List<Pair<String, @Composable () -> Unit>>,
    navController: NavHostController,
    selectedTabRoute: String,
) {
    Scaffold(
        bottomBar = {
            NavigationBarComposable(
                state = state,
                navController = navController,
                onIntent = onIntent,
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = selectedTabRoute,
            modifier = Modifier.padding(innerPadding),
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
    onIntent: (MainViewModel.Intent) -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        state.tabs.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.tabName) },
                label = { Text(item.tabName) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        onIntent(MainViewModel.Intent.OpenTab(item.route))
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
            onIntent = {},
            navController = rememberNavController(),
            navigationTabs = listOf(
                BottomNavigationTab.MyBooks.route to {
                    Text("tab1")
                },
                BottomNavigationTab.BooksStore.route to {
                    Text("tab2")
                },
                BottomNavigationTab.Settings.route to {
                    Text("tab3")
                }
            ),
            selectedTabRoute = BottomNavigationTab.MyBooks.route,
        )
    }
}