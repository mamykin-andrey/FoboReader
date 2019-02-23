package ru.mamykin.foboreader.data.repository.books

import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.domain.entity.FictionBook
import rx.Completable
import rx.Single
import javax.inject.Inject

class BooksRepository @Inject constructor(
        private val bookDao: BookDao,
        private val bookParser: BookParser
) {
    fun getBookInfo(bookPath: String): Single<FictionBook> = Single.just(
            bookDao.getBook(bookPath)
    )

    fun removeBook(bookPath: String): Completable = Completable.fromCallable {
        bookDao.getBook(bookPath)?.let(bookDao::delete)
    }

    fun getBooks(query: String, sortOrder: BookDao.SortOrder): Single<List<FictionBook>> = Single.fromCallable {
        bookDao.getBooks(query, sortOrder)
    }

    fun getBook(filePath: String): Single<FictionBook> = Single.create {
        val book = bookDao.getBook(filePath) ?: FictionBook().apply {
            this.filePath = filePath
        }
        bookParser.parse(book) {
            bookDao.update(book)
            it.onSuccess(book)
        }
    }

    fun updateBook(book: FictionBook): Completable = Completable.fromAction {
        bookDao.update(book)
    }
}