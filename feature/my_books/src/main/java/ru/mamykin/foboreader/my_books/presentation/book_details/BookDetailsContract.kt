package ru.mamykin.foboreader.my_books.presentation.book_details

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

sealed class Event {
    object ReadBookClicked : Event()
}

sealed class Effect {
    data class ShowSnackbar(val message: String) : Effect()
}

sealed class Action {
    data class BookLoaded(val bookInfo: BookInfo) : Action()
    object LoadingError : Action()
}

data class ViewState(
    val isLoading: Boolean = false,
    val bookInfo: BookInfo? = null,
    val error: Boolean = false
)