package ru.mamykin.foboreader.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

class MainFragment : BaseFragment() {

    override val featureName: String
        get() = "main"

    @Inject
    internal lateinit var feature: MainFeature

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
            )
        }.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            MainScreen(feature.state)
        }
    }

    @Composable
    private fun MainScreen(state: MainFeature.State) {
        val navController = rememberNavController()
        val bottomNavigationItems = listOf(
            BottomNavigationScreen.MyBooks,
            BottomNavigationScreen.BooksStore,
            BottomNavigationScreen.Settings,
        )
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val hasInitializedFragment = remember { mutableStateOf(false) }
        FoboReaderTheme {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        bottomNavigationItems.forEach { item ->
                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = item.tabName) },
                                label = { Text(item.tabName) },
                                selected = currentBackStackEntry?.destination?.route == item.destination.route,
                                onClick = {
                                    navigateToFragment(item.fragmentClassName)
                                }
                            )
                        }
                    }
                },
            ) { innerPadding ->
                AndroidView(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    factory = { context ->
                        FragmentContainerView(context).apply {
                            id = R.id.nav_host_fragment
                        }
                    },
                    update = { fragmentContainer ->
                        if (!hasInitializedFragment.value) {
                            childFragmentManager.beginTransaction()
                                .replace(
                                    fragmentContainer.id,
                                    Class.forName(bottomNavigationItems.first().fragmentClassName)
                                        .newInstance() as Fragment
                                )
                                .commit()
                            hasInitializedFragment.value = true
                        }
                    }

                )
            }
        }
    }

    private fun navigateToFragment(fragmentClassName: String) {
        val fragment = Class.forName(fragmentClassName).newInstance() as Fragment
        childFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    sealed class BottomNavigationScreen(
        val destination: BottomNavigationDestination,
        val tabName: String,
        val fragmentClassName: String,
        val icon: ImageVector,
    ) {
        object MyBooks : BottomNavigationScreen(
            BottomNavigationDestination.MyBooks,
            "Tab1",
            "ru.mamykin.foboreader.my_books.list.MyBooksFragment",
            Icons.Filled.Terrain,
        )

        object BooksStore : BottomNavigationScreen(
            BottomNavigationDestination.BooksStore,
            "Tab2",
            "ru.mamykin.foboreader.store.categories.BookCategoriesFragment",
            Icons.Filled.FoodBank,
        )

        object Settings : BottomNavigationScreen(
            BottomNavigationDestination.Settings,
            "Tab3",
            "ru.mamykin.foboreader.settings.all_settings.SettingsFragment",
            Icons.Filled.Fireplace,
        )
    }

    @Serializable
    sealed class BottomNavigationDestination(
        val route: String,
    ) {
        @Serializable
        object MyBooks : BottomNavigationDestination("my_books")

        @Serializable
        object BooksStore : BottomNavigationDestination("books_store")

        @Serializable
        object Settings : BottomNavigationDestination("settings")
    }
}