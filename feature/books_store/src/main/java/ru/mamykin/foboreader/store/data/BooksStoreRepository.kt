package ru.mamykin.foboreader.store.data

import ru.mamykin.foboreader.store.domain.model.StoreBook

class BooksStoreRepository constructor(
    private val service: BooksStoreService
) {
    private var booksCache: List<StoreBook>? = null

    suspend fun getBooks(query: String? = null): List<StoreBook> {
        val books = booksCache ?: service.getBooksAsync()
            .await()
            .books
            .map { it.toDomainModel() }
            .also { booksCache = it }

        return books.filterBooksByQuery(query)
    }

    private fun List<StoreBook>.filterBooksByQuery(query: String?): List<StoreBook> {
        query ?: return this
        return this.filter {
            it.title.contains(query, ignoreCase = true) || it.author.contains(
                query,
                ignoreCase = true
            )
        }
    }
}