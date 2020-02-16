package ru.mamykin.foboreader.my_books.data

import ru.mamykin.foboreader.my_books.data.database.BookInfoDao
import ru.mamykin.foboreader.my_books.domain.model.BookInfo

class MyBooksRepository constructor(
        private val bookInfoDao: BookInfoDao
) {
    suspend fun getBooks(query: String?): List<BookInfo> = with(bookInfoDao) {
        val books = if (query.isNullOrEmpty()) getBooks() else findBooks(query)
        return books.map { it.toDomainModel() }
    }

    suspend fun getBook(id: Long): BookInfo? {
        return bookInfoDao.getBook(id)?.toDomainModel()
    }

    suspend fun removeBook(id: Long) {
        bookInfoDao.remove(id)
    }
}