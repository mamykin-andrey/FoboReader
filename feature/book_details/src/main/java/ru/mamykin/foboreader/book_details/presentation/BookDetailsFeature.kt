package ru.mamykin.foboreader.book_details.presentation

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.book_details.domain.model.BookDetails
import ru.mamykin.foboreader.book_details.domain.usecase.GetBookDetails
import ru.mamykin.foboreader.book_details.presentation.model.BookInfoViewItem
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.platform.ResourceManager
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import javax.inject.Inject
import javax.inject.Named

internal class BookDetailsFeature @Inject constructor(
    actor: BookDetailsActor,
    reducer: BookDetailsReducer,
    private val uiEventTransformer: BookDetailsUiEventTransformer,
) : Feature<BookDetailsFeature.State, BookDetailsFeature.Intent, Nothing, BookDetailsFeature.Action>(
    State(),
    actor,
    reducer,
) {
    init {
        sendIntent(Intent.LoadBookInfo)
    }

    fun sendEvent(event: Event) {
        sendIntent(uiEventTransformer.invoke(event))
    }

    internal class BookDetailsUiEventTransformer @Inject constructor() {

        operator fun invoke(event: Event): Intent = when (event) {
            is Event.ReadBookClicked -> Intent.OpenBook
        }
    }

    internal class BookDetailsActor @Inject constructor(
        @Named("bookId") private val bookId: Long,
        private val router: Router,
        private val screenProvider: ScreenProvider,
        private val getBookDetails: GetBookDetails,
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.OpenBook -> {
                    router.navigateTo(screenProvider.readBookScreen(bookId))
                }
                is Intent.LoadBookInfo -> {
                    emit(Action.BookLoaded(getBookDetails.execute(bookId)))
                }
            }
        }
    }

    internal class BookDetailsReducer @Inject constructor(
        private val resourceManager: ResourceManager,
    ) : Reducer<State, Action, Nothing> {

        override fun invoke(state: State, action: Action): ReducerResult<State, Nothing> = when (action) {
            is Action.BookLoaded -> state.copy(
                isLoading = false,
                bookDetails = action.bookDetails,
                items = createInfoItems(action.bookDetails)
            ) to emptySet()
            is Action.LoadingError -> state.copy(
                isLoading = false,
                isError = true,
            ) to emptySet()
        }

        private fun createInfoItems(bookDetails: BookDetails) = listOf(
            BookInfoViewItem(
                resourceManager.getString(R.string.my_books_bookmarks),
                resourceManager.getString(R.string.my_books_no_bookmarks)
            ),
            BookInfoViewItem(
                resourceManager.getString(R.string.my_books_book_path),
                bookDetails.filePath
            ),
            BookInfoViewItem(
                resourceManager.getString(R.string.my_books_current_page),
                bookDetails.currentPage.toString()
            ),
            BookInfoViewItem(
                resourceManager.getString(R.string.my_books_book_genre),
                bookDetails.genre
            )
        )
    }

    sealed class Event {
        object ReadBookClicked : Event()
    }

    sealed class Intent {
        object LoadBookInfo : Intent()
        object OpenBook : Intent()
    }

    sealed class Action {
        class BookLoaded(val bookDetails: BookDetails) : Action()
        object LoadingError : Action()
    }

    data class State(
        val isLoading: Boolean = true,
        val bookDetails: BookDetails? = null,
        val items: List<BookInfoViewItem>? = null,
        val isError: Boolean = false,
    )
}