package ru.mamykin.foboreader.domain.bookdetails

import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.di.qualifiers.BookPath
import ru.mamykin.foboreader.entity.FictionBook
import rx.Single
import javax.inject.Inject

class BookDetailsInteractor @Inject constructor(
        private val repository: BooksRepository,
        @BookPath private val bookPath: String
) {

    fun loadBookInfo(): Single<FictionBook> {
        return repository.getBookInfo(bookPath)
    }

    fun getBookPath(): String = bookPath
}