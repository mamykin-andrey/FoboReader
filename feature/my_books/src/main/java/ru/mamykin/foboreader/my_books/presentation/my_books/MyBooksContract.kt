package ru.mamykin.foboreader.my_books.presentation.my_books

import ru.mamykin.foboreader.core.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.domain.my_books.SortOrder

sealed class Action {
    object Loading : Action()
    data class BooksLoaded(val books: List<BookInfo>) : Action()
}

data class ViewState(
    val isLoading: Boolean = false,
    val books: List<BookInfo> = emptyList()
)

sealed class Event {
    object ScanBooks : Event()
    object LoadBooks : Event()
    data class RemoveBook(val id: Long) : Event()
    data class SortBooks(val sortOrder: SortOrder) : Event()
    data class FilterBooks(val query: String) : Event()
}

sealed class Effect