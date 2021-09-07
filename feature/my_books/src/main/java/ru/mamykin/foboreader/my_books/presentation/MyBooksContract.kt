package ru.mamykin.foboreader.my_books.presentation

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.domain.model.SortOrder

sealed class Action {
    object Loading : Action()
    data class BooksLoaded(val books: List<BookInfo>) : Action()
}

sealed class ViewState {
    object Loading : ViewState()

    object Empty : ViewState()

    data class Success(
        val books: List<BookInfo>
    ) : ViewState()
}

sealed class Event {
    data class RemoveBook(val id: Long) : Event()
    data class SortBooks(val sortOrder: SortOrder) : Event()
    data class FilterBooks(val query: String) : Event()
}

sealed class Effect