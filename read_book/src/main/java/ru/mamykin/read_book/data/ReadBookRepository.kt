package ru.mamykin.read_book.data

import ru.mamykin.core.data.BookParser
import ru.mamykin.core.data.model.FictionBook

class ReadBookRepository(
        private val bookParser: BookParser
) {
    // loadBook вместо getBook
    suspend fun getBook(filePath: String): FictionBook {
        return FictionBook()
//        val book = bookDao.getBook(filePath)
//                ?: FictionBook().apply { this.filePath = filePath }
//        bookParser.parse(book)
//        bookDao.update(book)
//        return book
    }
}