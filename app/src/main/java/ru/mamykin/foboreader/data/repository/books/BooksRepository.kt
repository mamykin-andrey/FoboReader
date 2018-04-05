package ru.mamykin.foboreader.data.repository.books

import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.domain.readbook.BookXmlSaxParser
import ru.mamykin.foboreader.entity.FictionBook
import rx.Completable
import rx.Single
import javax.inject.Inject

class BooksRepository @Inject constructor(
        private val bookDao: BookDao
) {
    fun getBookInfo(bookPath: String): Single<FictionBook> {
        return Single.just(bookDao.getBook(bookPath))
    }

    fun removeBook(bookPath: String): Completable {
        return Completable.fromCallable {
            val book = bookDao.getBook(bookPath)
            bookDao.delete(book!!)
        }
    }

    fun getBooks(query: String, sortOrder: BookDao.SortOrder): Single<List<FictionBook>> {
        return Single.fromCallable { bookDao.getBooks(query, sortOrder) }
    }

    fun getBook(bookPath: String): Single<FictionBook> {
        return Single.create {
            val book = bookDao.getBook(bookPath) ?: createBook(bookPath)
            BookXmlSaxParser.parseBook(book, {
                bookDao.update(book)
                it.onSuccess(book)
            })
        }
    }

    fun updateBook(book: FictionBook): Completable {
        return Completable.fromAction {
            bookDao.update(book)
        }
    }

    private fun createBook(filePath: String) = FictionBook().apply { this.filePath = filePath }
}