package ru.mamykin.foboreader.store.data

import ru.mamykin.foboreader.core.data.InMemoryCache
import ru.mamykin.foboreader.store.data.network.BooksStoreService
import ru.mamykin.foboreader.store.domain.model.StoreBook

class BooksStoreRepository constructor(
    private val service: BooksStoreService
) {
    private val memoryCache = InMemoryCache<List<StoreBook>>()

    suspend fun getBooks(query: String? = null): List<StoreBook> {
        val books = memoryCache.getOrFetch {
            service.getBooksAsync()
                .await()
                .books
                .map { it.toDomainModel() }
        }
        return books.filterBooksByQuery(query)
    }

    private fun List<StoreBook>.filterBooksByQuery(query: String?): List<StoreBook> {
        query ?: return this
        return filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.author.contains(query, ignoreCase = true)
        }
    }
}