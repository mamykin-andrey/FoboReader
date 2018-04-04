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
    fun getBookInfo(bookId: Int): Single<FictionBook> {
        return Single.just(bookDao.getBook(bookId))
    }

    fun removeBook(bookId: Int): Completable {
        return Completable.fromCallable {
            val book = bookDao.getBook(bookId)
            return@fromCallable bookDao.delete(book)
        }
    }

    fun getBooks(searchQuery: String, sortOrder: BookDao.SortOrder): Single<List<FictionBook>> {
        return Single.fromCallable {
            return@fromCallable bookDao.getBooks(searchQuery, sortOrder)
        }
    }

    fun getBook(bookId: Int): Single<FictionBook> {
        return Single.create {
            val book = bookDao.getBook(bookId)
            book!!.lastOpen = System.currentTimeMillis()
            bookDao.update(book)

            BookXmlSaxParser.parseBook(book, { it.onSuccess(book) })
        }
    }

    fun getBook(bookPath: String): Single<FictionBook> {
        return Single.create {
            val book = bookDao.getBook(bookPath)
            book.filePath = bookPath
            book.lastOpen = System.currentTimeMillis()
            bookDao.update(book)

            BookXmlSaxParser.parseBook(book, { it.onSuccess(book) })
        }
    }
}