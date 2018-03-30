package ru.mamykin.foboreader.domain.mybooks

import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.model.FictionBook
import ru.mamykin.foboreader.data.repository.BooksRepository
import rx.Completable
import rx.Single
import javax.inject.Inject

class MyBooksInteractor @Inject constructor(
        private val repository: BooksRepository
) {
    fun removeBook(bookId: Int): Completable {
        return repository.removeBook(bookId)
    }

    fun getBooks(searchQuery: String, sortOrder: BookDao.SortOrder): Single<List<FictionBook>> {
        return repository.getBooks(searchQuery, sortOrder)
    }

    fun getBook(bookId: Int): Single<FictionBook> {
        return repository.getBook(bookId)
    }
}