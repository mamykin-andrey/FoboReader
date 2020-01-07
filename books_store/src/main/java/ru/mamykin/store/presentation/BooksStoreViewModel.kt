package ru.mamykin.store.presentation

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.core.content.getSystemService
import kotlinx.coroutines.launch
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.store.domain.BooksStoreInteractor
import ru.mamykin.store.domain.model.StoreBook
import java.io.File

class BooksStoreViewModel(
        private val interactor: BooksStoreInteractor,
        private val context: Context
) : BaseViewModel<BooksStoreViewModel.ViewState, BooksStoreViewModel.Action, String>(
        ViewState(isLoading = true)
) {
    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.LoadingError -> state.copy(isLoading = false, isError = true)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.LoadBooks -> loadBooks()
            is Event.BookClicked -> downloadBook(event.book, event.downloadsDir)
        }
    }

    private fun loadBooks() = launch {
        runCatching { interactor.getBooks() }
                .onSuccess { onAction(Action.BooksLoaded(it)) }
                .onFailure { onAction(Action.LoadingError) }
    }

    private fun downloadBook(book: StoreBook, downloadsDir: File) {
        val bookFile = File(downloadsDir, book.getBookName())

        val request = DownloadManager.Request(Uri.parse(book.link))
                .setTitle(book.title)
                .setDescription("Загрузка книги")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(bookFile))

        val downloadManager: DownloadManager? = context.getSystemService()
        downloadManager?.enqueue(request)
    }

    sealed class Action {
        data class BooksLoaded(val books: List<StoreBook>) : Action()
        object LoadingError : Action()
    }

    sealed class Event {
        data class BookClicked(val book: StoreBook, val downloadsDir: File) : Event()
        object LoadBooks : Event()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val books: List<StoreBook> = emptyList(),
            val isError: Boolean = false
    )
}