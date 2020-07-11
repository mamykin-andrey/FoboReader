package ru.mamykin.foboreader.core.data.repository

import ru.mamykin.foboreader.core.data.database.BookInfoDao
import ru.mamykin.foboreader.core.data.model.toDatabaseModel
import ru.mamykin.foboreader.core.domain.model.BookInfo

class BookInfoRepository constructor(
    private val bookInfoDao: BookInfoDao
) {
    private val booksCache = BooksCache()

    suspend fun getBooks(): List<BookInfo> {
        return booksCache.getBooks() ?: bookInfoDao.getBooks()
            .map { it.toDomainModel() }
            .also(booksCache::setCache)
    }

    suspend fun findBooks(query: String): List<BookInfo> {
        return booksCache.findBooks(query)
            ?: bookInfoDao.findBooks(query)
                .map { it.toDomainModel() }
    }

    suspend fun getBookInfo(id: Long): BookInfo? {
        return booksCache.getBookInfo(id)
            ?: bookInfoDao.getBook(id)
                ?.toDomainModel()
    }

    suspend fun removeBook(id: Long) {
        bookInfoDao.remove(id)
    }

    suspend fun updateCurrentPage(bookId: Long, currentPage: Int) {
        bookInfoDao.updateCurrentPage(bookId, currentPage)
    }
}