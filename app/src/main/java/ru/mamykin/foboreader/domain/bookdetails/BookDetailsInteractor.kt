package ru.mamykin.foboreader.domain.bookdetails

import kotlinx.coroutines.runBlocking
import ru.mamykin.foboreader.core.di.qualifiers.BookPath
import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.domain.entity.FictionBook
import rx.Single
import javax.inject.Inject

class BookDetailsInteractor @Inject constructor(
        private val repository: BooksRepository,
        @BookPath private val bookPath: String
) {
    fun getBookInfo(): Single<FictionBook> {
        return Single.fromCallable { runBlocking { repository.getBookInfo(bookPath) } }
    }

    fun getBookPath(): String = bookPath
}