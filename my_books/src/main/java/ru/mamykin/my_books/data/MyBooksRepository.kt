package ru.mamykin.my_books.data

import ru.mamykin.core.data.BookParser
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.data.model.FictionBook

class MyBooksRepository constructor(
        private val bookDao: BookDao,
        private val bookParser: BookParser
) {
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
}