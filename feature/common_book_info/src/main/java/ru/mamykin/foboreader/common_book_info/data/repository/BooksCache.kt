package ru.mamykin.foboreader.common_book_info.data.repository

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

class BooksCache {

    private var cache: List<BookInfo>? = null

    fun setCache(cache: List<BookInfo>) {
        this.cache = cache
    }

    fun getBooks(): List<BookInfo>? = cache

    fun findBooks(query: String): List<BookInfo>? =
        cache?.filter { it.title.contains(query, ignoreCase = true) }

    fun getBookInfo(id: Long): BookInfo? = cache?.find { it.id == id }
}