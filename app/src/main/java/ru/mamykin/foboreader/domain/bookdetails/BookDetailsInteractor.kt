package ru.mamykin.foboreader.domain.bookdetails

import ru.mamykin.foboreader.data.model.FictionBook
import ru.mamykin.foboreader.data.repository.BookRepository
import rx.Single
import javax.inject.Inject

class BookDetailsInteractor @Inject constructor(
        private val repository: BookRepository,
        private val bookId: Int
) {

    fun loadBookInfo(): Single<FictionBook> {
        return repository.getBookInfo(bookId)
    }

    fun getBookId(): Int {
        return bookId
    }
}