package ru.mamykin.foboreader.book_details.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.common_book_info.domain.GetBookInfoUseCase
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class BookDetailsViewModel @Inject constructor(
    private val getBookInfoUseCase: GetBookInfoUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId: Long = savedStateHandle.get<Long>("bookId")!!
    private val isReadButtonEnabled: Boolean = savedStateHandle.get<Boolean>("readAllowed")!!

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
                if (state is State.Content) return@launch
                state = State.Content(
                    bookDetails = getBookInfoUseCase.execute(bookId)
                        .let(BookInfoUIModel::fromDomainModel),
                    isReadButtonEnabled = isReadButtonEnabled,
                )
            }
        }
    }

    sealed class Intent {
        data object LoadBookInfo : Intent()
        data object OpenBook : Intent()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val bookDetails: BookInfoUIModel,
            val isReadButtonEnabled: Boolean,
        ) : State()
    }

    sealed class Effect {
        data class NavigateToReadBook(val bookId: Long) : Effect()
    }
}