package ru.mamykin.my_books.data

import ru.mamykin.my_books.data.database.BookInfoDao
import ru.mamykin.my_books.domain.model.BookInfo

class MyBooksRepository constructor(
        private val bookInfoDao: BookInfoDao
) {
    suspend fun getBooks(): List<BookInfo> {
        return bookInfoDao.getBooks().map { it.toDomainModel() }
    }

    suspend fun findBooks(query: String): List<BookInfo> {
        return bookInfoDao.findBooks(query).map { it.toDomainModel() }
    }

    suspend fun getBook(id: Long): BookInfo? {
        return bookInfoDao.getBook(id)?.toDomainModel()
    }

    suspend fun removeBook(id: Long) {
        bookInfoDao.getBook(id)?.let { bookInfoDao.delete(it) }
    }
}