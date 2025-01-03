package ru.mamykin.foboreader.book_details.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class BookDetailsViewModel @Inject constructor(
    private val getBookDetails: GetBookDetails,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId: Long = savedStateHandle.get<Long>("bookId")!!

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()
    private var isDataLoaded = false

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.OpenBook -> {
                effectChannel.send(Effect.NavigateToReadBook(bookId))
            }

            is Intent.LoadBookInfo -> {
                if (isDataLoaded) return@launch
                state = State.Loaded(bookDetails = getBookDetails.execute(bookId))
                isDataLoaded = true
            }
        }
    }

    sealed class Intent {
        data object LoadBookInfo : Intent()
        data object OpenBook : Intent()
    }

    sealed class State {
        data object Loading : State()
        data class Loaded(
            val bookDetails: BookDetails,
        ) : State()
    }

    sealed class Effect {
        data class NavigateToReadBook(val bookId: Long) : Effect()
    }
}