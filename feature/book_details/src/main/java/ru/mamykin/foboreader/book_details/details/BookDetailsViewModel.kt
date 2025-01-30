package ru.mamykin.foboreader.book_details.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class BookDetailsViewModel @Inject constructor(
    private val getBookInfoUseCase: GetBookInfoUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<BookDetailsViewModel.Intent, BookDetailsViewModel.State, BookDetailsViewModel.Effect>(
    State.Loading
) {

    private val bookId: Long = savedStateHandle.get<Long>("bookId")!!
    private val isReadButtonEnabled: Boolean = savedStateHandle.get<Boolean>("readAllowed")!!

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.OpenBook -> {
                sendEffect(Effect.NavigateToReadBook(bookId))
            }

            is Intent.LoadBookInfo -> {
                state = State.Content(
                    bookDetails = getBookInfoUseCase.execute(bookId)
                        .let(BookInfoUIModel::fromDomainModel),
                    isReadButtonEnabled = isReadButtonEnabled,
                )
            }

            is Intent.RateBook -> {
                val contentState = (state as? State.Content) ?: return
                state = contentState.copy(isRateDialogShown = true)
            }

            is Intent.SaveBookRating -> {
                state = State.Content(
                    bookDetails = getBookInfoUseCase.execute(bookId)
                        .let(BookInfoUIModel::fromDomainModel),
                    isReadButtonEnabled = isReadButtonEnabled,
                    isRateDialogShown = false,
                )
            }
        }
    }

    sealed class Intent {
        data object LoadBookInfo : Intent()
        data object OpenBook : Intent()
        data object RateBook : Intent()
        data class SaveBookRating(val rating: Int?) : Intent()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val bookDetails: BookInfoUIModel,
            val isReadButtonEnabled: Boolean,
            val isRateDialogShown: Boolean = false,
        ) : State()
    }

    sealed class Effect {
        data class NavigateToReadBook(val bookId: Long) : Effect()
    }
}