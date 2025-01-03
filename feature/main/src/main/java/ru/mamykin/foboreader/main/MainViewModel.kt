package ru.mamykin.foboreader.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor() : ViewModel() {

    var state: State by LoggingStateDelegate(State())
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.OpenTab -> {
                effectChannel.send(Effect.NavigateToTab(intent.route))
            }
        }
    }

    sealed class Intent {
        data class OpenTab(val route: String) : Intent()
    }

    sealed class Effect {
        data class NavigateToTab(val route: String) : Effect()
    }

    data class State(
        val tabs: List<BottomNavigationTab> = listOf(
            BottomNavigationTab(
                route = BottomNavigationTabRoute.MY_BOOKS,
                tabNameId = R.string.app_my_books_tab,
                icon = Icons.Filled.Book
            ),
            BottomNavigationTab(
                route = BottomNavigationTabRoute.BOOKS_STORE,
                tabNameId = R.string.app_books_store_tab,
                icon = Icons.Filled.Storefront
            ),
            BottomNavigationTab(
                route = BottomNavigationTabRoute.SETTINGS,
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