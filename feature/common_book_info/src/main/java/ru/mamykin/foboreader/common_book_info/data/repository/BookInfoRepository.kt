package ru.mamykin.foboreader.common_book_info.data.repository

import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

class BookInfoRepository constructor(
    private val bookInfoDao: BookInfoDao
) {
    suspend fun getBooks(): List<BookInfo> {
        return bookInfoDao.getBooks()
            .map { it.toDomainModel() }
    }

    suspend fun findBooks(query: String): List<BookInfo> {
        return bookInfoDao.findBooks(query)
            .map { it.toDomainModel() }
    }

    suspend fun getBookInfo(id: Long): BookInfo? {
        return bookInfoDao.getBook(id)
            ?.toDomainModel()
    }

    suspend fun removeBook(id: Long) {
        bookInfoDao.remove(id)
    }

    suspend fun updateCurrentPage(bookId: Long, currentPage: Int) {
        bookInfoDao.updateCurrentPage(bookId, currentPage)
    }
}