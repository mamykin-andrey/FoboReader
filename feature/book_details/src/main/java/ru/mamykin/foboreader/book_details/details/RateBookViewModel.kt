package ru.mamykin.foboreader.book_details.details

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.common_book_info.domain.GetBookInfoUseCase
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import javax.inject.Inject

@HiltViewModel
internal class RateBookViewModel @Inject constructor(
    private val getBookInfoUseCase: GetBookInfoUseCase,
) : BaseViewModel<RateBookViewModel.Intent>() {

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadBookInfo -> {
                state = State.Content(
                    getBookInfoUseCase.execute(intent.bookId)
                        .let(BookInfoUIModel::fromDomainModel)
                )
            }
        }
    }

    sealed class Intent {
        data class LoadBookInfo(val bookId: Long) : Intent()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val bookDetails: BookInfoUIModel,
        ) : State()

        data object Failed : State()
    }
}