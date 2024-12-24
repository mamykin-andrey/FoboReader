package ru.mamykin.foboreader.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@MainScope
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
            BottomNavigationTab.MyBooks,
            BottomNavigationTab.BooksStore,
            BottomNavigationTab.Settings
        )
    )

    sealed class BottomNavigationTab(
        val route: String,
        val tabName: String,
        val icon: ImageVector,
    ) {
        data object MyBooks : BottomNavigationTab(
            "my_books",
            "My books",
            Icons.Filled.Book,
        )

        data object BooksStore : BottomNavigationTab(
            "books_store",
            "Books store",
            Icons.Filled.Storefront,
        )

        data object Settings : BottomNavigationTab(
            "settings",
            "Settings",
            Icons.Filled.Settings,
        )
    }
}