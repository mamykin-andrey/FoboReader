package ru.mamykin.foboreader.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import javax.inject.Inject

@MainScope
internal class MainFeature @Inject constructor(
    actor: MainActor,
    reducer: MainReducer,
    scope: CoroutineScope,
) : ComposeFeature<MainFeature.State, MainFeature.Intent, MainFeature.Effect, MainFeature.Action>(
    State(),
    actor,
    reducer,
    scope,
) {
    internal class MainActor @Inject constructor() : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.OpenTab -> {
                    emit(Action.TabChanged(intent.route))
                }
            }
        }
    }

    internal class MainReducer @Inject constructor(
    ) : Reducer<State, Action, Effect> {

        override fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.TabChanged -> state to setOf(Effect.NavigateToTab(action.route))
        }
    }

    sealed class Intent {
        data class OpenTab(val route: String) : Intent()
    }

    sealed class Effect {
        data class NavigateToTab(val route: String) : Effect()
    }

    sealed class Action {
        data class TabChanged(val route: String) : Action()
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