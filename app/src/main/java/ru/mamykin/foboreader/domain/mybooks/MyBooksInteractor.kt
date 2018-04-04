package ru.mamykin.foboreader.domain.mybooks

import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.entity.FictionBook
import rx.Completable
import rx.Single
import javax.inject.Inject

class MyBooksInteractor @Inject constructor(
        private val repository: BooksRepository
) {
    fun removeBook(bookPath: String): Completable {
        return repository.removeBook(bookPath)
    }

    fun getBooks(searchQuery: String, sortOrder: BookDao.SortOrder): Single<List<FictionBook>> {
        return repository.getBooks(searchQuery, sortOrder)
    }

    fun getBook(bookPath: String): Single<FictionBook> {
        return repository.getBook(bookPath)
    }
}