package ru.mamykin.foboreader.data.repository.books

import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.domain.entity.FictionBook
import javax.inject.Inject

class BooksRepository @Inject constructor(
        private val bookDao: BookDao,
        private val bookParser: BookParser
) {
    suspend fun getBookInfo(bookPath: String): FictionBook {
        return bookDao.getBook(bookPath)!!
    }

    suspend fun removeBook(bookPath: String) {
        bookDao.getBook(bookPath)?.let { bookDao.delete(it) }
    }

    suspend fun getBooks(query: String, sortOrder: BookDao.SortOrder): List<FictionBook> {
        return bookDao.getBooks(query, sortOrder)
    }

    // loadBook вместо getBook
    suspend fun getBook(filePath: String): FictionBook {
        val book = bookDao.getBook(filePath)
                ?: FictionBook().apply { this.filePath = filePath }
        bookParser.parse(book)
        bookDao.update(book)
        return book
    }

    suspend fun updateBook(book: FictionBook) {
        bookDao.update(book)
    }
}