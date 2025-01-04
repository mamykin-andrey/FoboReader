package ru.mamykin.foboreader.store.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class StoreSearchViewModel @Inject constructor() : ViewModel() {

    var state: State by LoggingStateDelegate(State)
        private set

    // private val effectChannel = LoggingEffectChannel<Effect>()
    // val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        // when (intent) {
        // is Intent.SwitchTab -> {
        // state = State.Content(currentRoute = BooksStoreScreen.BooksStoreList.createRoute("1"))
        // }
        // }
    }

    sealed class Intent {
        // data object SwitchTab : Intent()
    }

    data object State
}