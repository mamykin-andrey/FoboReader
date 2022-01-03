package ru.mamykin.foboreader.book_details.presentation

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.book_details.domain.usecase.GetBookDetails
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import javax.inject.Inject
import javax.inject.Named

class BookDetailsViewModel @Inject constructor(
    @Named("bookId") private val bookId: Long,
    private val getBookDetails: GetBookDetails,
    private val router: Router,
    private val screenProvider: ScreenProvider
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    init {
        viewModelScope.launch {
            getBookDetails.execute(bookId)
                .doOnSuccess { sendAction(Action.BookLoaded(it)) }
                .doOnError { sendAction(Action.LoadingError) }
        }
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.BookLoaded -> state.copy(isLoading = false, bookInfo = action.bookInfo)
        is Action.LoadingError -> state.copy(isLoading = false, error = true)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.ReadBookClicked -> {
                router.navigateTo(screenProvider.readBookScreen(bookId))
            }
        }
    }
}