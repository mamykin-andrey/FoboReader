package ru.mamykin.foboreader.read_book.data

import ru.mamykin.foboreader.read_book.data.model.FictionBook

class ReadBookRepository {

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