package ru.mamykin.foboreader.store.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.mamykin.foboreader.store.data.network.BooksStoreService
import ru.mamykin.foboreader.store.domain.model.StoreBook

class BooksStoreRepository(
    private val service: BooksStoreService
) {
    private var allBooks: List<StoreBook>? = null
    private var books: List<StoreBook> = emptyList()
    private val loadBooksMutex = Mutex()

    suspend fun getBooks(query: String): List<StoreBook> {
        val allBooks = this.allBooks ?: loadBooks()
        return filter(allBooks, query)
            .also { this.books = it }
    }

    private suspend fun loadBooks(): List<StoreBook> = loadBooksMutex.withLock {
        return service.getBooksAsync()
            .await()
            .books
            .map { it.toDomainModel() }
            .also { allBooks = it }
    }

    private fun filter(allBooks: List<StoreBook>, query: String): List<StoreBook> {
        return allBooks.filter { it.containsText(query) }
    }
}