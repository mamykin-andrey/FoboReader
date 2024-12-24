package ru.mamykin.foboreader.book_details.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject
import javax.inject.Named

internal class BookDetailsViewModel @Inject constructor(
    @Named("bookId") private val bookId: Long,
    private val getBookDetails: GetBookDetails
) : ViewModel() {

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.OpenBook -> {
                effectChannel.send(Effect.NavigateToReadBook(bookId))
            }

            is Intent.LoadBookInfo -> {
                state = State.Loaded(bookDetails = getBookDetails.execute(bookId))
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