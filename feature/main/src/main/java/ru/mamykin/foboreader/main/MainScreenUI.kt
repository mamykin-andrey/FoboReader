package ru.mamykin.foboreader.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.getActivity
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme

// TODO: Fix blinking when a tab switched
private const val SCREEN_KEY = "main"

private lateinit var tabFragmentProvider: TabComposableProvider

private fun createAndInitViewModel(apiHolder: ApiHolder): MainViewModel {
    val component = ComponentHolder.getOrCreateComponent(SCREEN_KEY) {
        DaggerMainComponent.factory().create(
            apiHolder.commonApi(),
            apiHolder.mainApi()
        )
    }
    // TODO: Refactor this
    tabFragmentProvider = component.tabComposableProvider()
    return component.viewModel()
}

@Composable
fun MainScreenUI(
    onBookCategoryClick: (String) -> Unit,
    onBookDetailsClick: (Long) -> Unit,
    onReadBookClick: (Long) -> Unit,
    onNightThemeSwitch: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val viewModel = remember { createAndInitViewModel(context.getActivity().apiHolder()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        onDispose {
            if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                ComponentHolder.clearComponent(SCREEN_KEY)
            }
        }
    }
    val navController = rememberNavController()
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, navController = navController)
        }
    }
    MainScreenComposable(
        state = viewModel.state,
        onIntent = viewModel::sendIntent,
        onBookCategoryClick = onBookCategoryClick,
        onBookDetailsClick = onBookDetailsClick,
        onReadBookClick = onReadBookClick,
        onNightThemeSwitch = onNightThemeSwitch,
        navController = navController,
    )
}

private fun takeEffect(effect: MainViewModel.Effect, navController: NavHostController) = when (effect) {
    is MainViewModel.Effect.NavigateToTab -> {
        navigateToScreen(navController, effect.route)
    }
}

@Composable
private fun MainScreenComposable(
    state: MainViewModel.State,
    onIntent: (MainViewModel.Intent) -> Unit,
    onBookCategoryClick: (String) -> Unit,
    onBookDetailsClick: (Long) -> Unit,
    onReadBookClick: (Long) -> Unit,
    navController: NavHostController,
    onNightThemeSwitch: (Boolean) -> Unit,
) {
    Scaffold(
        bottomBar = {
            NavigationBarComposable(
                state,
                navController,
                onIntent,
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainViewModel.BottomNavigationTab.MyBooks.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainViewModel.BottomNavigationTab.MyBooks.route) {
                tabFragmentProvider.MyBooksScreenTabContent(onBookDetailsClick, onReadBookClick)
            }
            composable(MainViewModel.BottomNavigationTab.BooksStore.route) {
                tabFragmentProvider.BooksStoreScreenTabContent(onBookCategoryClick)
            }
            composable(MainViewModel.BottomNavigationTab.Settings.route) {
                tabFragmentProvider.SettingsScreenTabContent(onNightThemeSwitch)
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

@Preview
@Composable
fun MainScreenPreview() {
    FoboReaderTheme {
        MainScreenComposable(
            state = MainViewModel.State(),
            onIntent = {},
            onBookCategoryClick = {},
            onBookDetailsClick = {},
            onReadBookClick = {},
            navController = rememberNavController(),
            onNightThemeSwitch = {},
        )
    }
}