package ru.mamykin.foboreader.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.navigation.MainTabScreenRoutes
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor() : ViewModel() {

    var state: State by LoggingStateDelegate(State())
        private set

    data class State(
        val tabs: List<BottomNavigationTab> = listOf(
            BottomNavigationTab(
                route = MainTabScreenRoutes.MY_BOOKS,
                tabNameId = R.string.app_my_books_tab,
                icon = Icons.Filled.Book
            ),
            BottomNavigationTab(
                route = MainTabScreenRoutes.BOOKS_STORE,
                tabNameId = R.string.app_books_store_tab,
                icon = Icons.Filled.Storefront
            ),
            BottomNavigationTab(
                route = MainTabScreenRoutes.SETTINGS,
                tabNameId = R.string.app_books_store_tab,
                icon = Icons.Filled.Settings
            ),
        ),
    )

    data class BottomNavigationTab(
        val route: String,
        @StringRes val tabNameId: Int,
        val icon: ImageVector,
    )
}