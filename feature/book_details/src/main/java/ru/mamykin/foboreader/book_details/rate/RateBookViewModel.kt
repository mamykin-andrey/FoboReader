package ru.mamykin.foboreader.book_details.rate

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.book_details.details.BookInfoUIModel
import ru.mamykin.foboreader.common_book_info.domain.GetBookInfoUseCase
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class RateBookViewModel @Inject constructor(
    private val getBookInfoUseCase: GetBookInfoUseCase,
    private val rateBookUseCase: RateBookUseCase,
) : BaseViewModel<RateBookViewModel.Intent, RateBookViewModel.State, RateBookViewModel.Effect>(
    State.Loading
) {
    private var bookId: Long? = null

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadBookInfo -> {
                this.bookId = intent.bookId
                state = State.Content(
                    getBookInfoUseCase.execute(intent.bookId)
                        .let(BookInfoUIModel.Companion::fromDomainModel)
                )
            }

            is Intent.ReloadBookInfo -> handleIntent(Intent.LoadBookInfo(requireNotNull(bookId)))

            is Intent.SelectRating -> {
                state = State.Content(
                    bookDetails = getBookInfoUseCase.execute(requireNotNull(bookId))
                        .let(BookInfoUIModel.Companion::fromDomainModel),
                    selectedRating = intent.rating,
                )
            }

            is Intent.SubmitRating -> submitRating()
        }
    }

    private suspend fun submitRating() {
        val contentState = (state as? State.Content) ?: return
        val selectedRating = contentState.selectedRating ?: return

        state = contentState.copy(isSubmitInProgress = true)
        rateBookUseCase.execute(requireNotNull(bookId), selectedRating).fold(
            onSuccess = { sendEffect(Effect.CloseScreen(selectedRating)) },
            onFailure = { state = contentState.copy(isSubmitInProgress = false) },
        )
    }

    sealed class Intent {
        data class LoadBookInfo(val bookId: Long) : Intent()
        data object ReloadBookInfo : Intent()
        data class SelectRating(val rating: Int) : Intent()
        data object SubmitRating : Intent()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val bookDetails: BookInfoUIModel,
            val selectedRating: Int? = null,
            val isSubmitInProgress: Boolean = false,
        ) : State()

        data object Failed : State()
    }

    sealed class Effect {
        data class CloseScreen(val rating: Int) : Effect()
    }
}