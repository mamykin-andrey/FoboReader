package ru.mamykin.foboreader.book_details.rate

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.book_details.details.BookDetailsViewModel
import ru.mamykin.foboreader.book_details.details.BookInfoUIModel
import ru.mamykin.foboreader.common_book_info.domain.GetBookInfoUseCase
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class RateBookViewModel @Inject constructor(
    private val getBookInfoUseCase: GetBookInfoUseCase,
) : BaseViewModel<RateBookViewModel.Intent>() {

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private var bookId: Long? = null

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadBookInfo -> {
                this.bookId = intent.bookId
                state = State.Content(
                    bookDetails = getBookInfoUseCase.execute(intent.bookId)
                        .let(BookInfoUIModel.Companion::fromDomainModel),
                    selectedRating = null,
                )
            }

            is Intent.SelectRating -> {
                state = State.Content(
                    bookDetails = getBookInfoUseCase.execute(requireNotNull(bookId))
                        .let(BookInfoUIModel.Companion::fromDomainModel),
                    selectedRating = intent.rating,
                )
            }

            is Intent.SubmitRating -> {
                val rating = (state as? State.Content)?.selectedRating ?: return
                effectChannel.send(Effect.CloseScreen(rating))
            }
        }
    }

    sealed class Intent {
        data class LoadBookInfo(val bookId: Long) : Intent()
        data class SelectRating(val rating: Int) : Intent()
        data object SubmitRating : Intent()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val bookDetails: BookInfoUIModel,
            val selectedRating: Int?,
        ) : State()

        data object Failed : State()
    }

    sealed class Effect {
        data class CloseScreen(val rating: Int) : Effect()
    }
}