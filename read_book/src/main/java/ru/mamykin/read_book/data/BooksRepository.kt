package ru.mamykin.read_book.data

import ru.mamykin.core.data.BookParser
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.data.model.FictionBook
import javax.inject.Inject

class BooksRepository @Inject constructor(
        private val bookDao: BookDao,
        private val bookParser: BookParser
) {
    // loadBook вместо getBook
    suspend fun getBook(filePath: String): FictionBook {
        val book = bookDao.getBook(filePath)
                ?: FictionBook().apply { this.filePath = filePath }
        bookParser.parse(book)
        bookDao.update(book)
        return book
    }
}