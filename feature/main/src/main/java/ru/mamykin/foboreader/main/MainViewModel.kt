package ru.mamykin.foboreader.main

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
            BottomNavigationTab.MyBooks,
            BottomNavigationTab.BooksStore,
            BottomNavigationTab.Settings
        )
    )
}