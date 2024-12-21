package ru.mamykin.foboreader.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

private typealias TabContentProvider = @Composable () -> Unit

class MainFragment : BaseFragment() {

    override val featureName: String
        get() = "main"

    @Inject
    internal lateinit var feature: MainFeature

    @Inject
    internal lateinit var tabFragmentProvider: TabComposableProvider

    private val tabContentProviders = mapOf<String, TabContentProvider>(
        MainFeature.BottomNavigationTab.MyBooks.route to {
            tabFragmentProvider.MyBooksScreenTabContent()
        },
        MainFeature.BottomNavigationTab.BooksStore.route to {
            tabFragmentProvider.BooksStoreScreenTabContent()
        },
        MainFeature.BottomNavigationTab.Settings.route to {
            tabFragmentProvider.SettingsScreenTabContent()
        },
    )

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerMainComponent.factory().create(
                commonApi(),
                apiHolder().mainApi()
            )
        }.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            MainScreen(feature.state, feature.effectFlow)
        }
    }

    private fun takeEffect(effect: MainFeature.Effect, navController: NavHostController) = when (effect) {
        is MainFeature.Effect.NavigateToTab -> {
            navigateToScreen(navController, effect.route)
        }
    }

    @Composable
    private fun MainScreen(state: MainFeature.State, effectFlow: Flow<MainFeature.Effect>) {
        val navController = rememberNavController()

        LaunchedEffect(effectFlow) {
            effectFlow.collect {
                takeEffect(it, navController)
            }
        }

        FoboReaderTheme {
            Scaffold(
                bottomBar = {
                    NavigationBarComposable(state, navController)
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = MainFeature.BottomNavigationTab.MyBooks.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    state.tabs.forEach { item ->
                        composable(item.route) {
                            tabContentProviders[item.route]!!.invoke()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun NavigationBarComposable(state: MainFeature.State, navController: NavHostController) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route
        NavigationBar(
            containerColor = MaterialTheme.colors.primaryVariant
        ) {
            state.tabs.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.tabName) },
                    label = { Text(item.tabName) },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            feature.sendIntent(MainFeature.Intent.OpenTab(item.route))
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
        MainScreen(state = MainFeature.State(), effectFlow = emptyFlow())
    }
}