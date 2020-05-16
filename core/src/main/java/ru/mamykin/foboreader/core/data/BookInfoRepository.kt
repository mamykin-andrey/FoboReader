package ru.mamykin.foboreader.core.data

import ru.mamykin.foboreader.core.data.database.BookInfoDao
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

    private class BooksCache {

        private var cache: List<BookInfo>? = null

        fun setCache(cache: List<BookInfo>) {
            this.cache = cache
        }

        fun getBooks(): List<BookInfo>? = cache

        fun findBooks(query: String): List<BookInfo>? =
                cache?.filter { it.title.contains(query, ignoreCase = true) }

        fun getBookInfo(id: Long): BookInfo? = cache?.find { it.id == id }
    }
}