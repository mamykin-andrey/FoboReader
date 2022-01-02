package ru.mamykin.foboreader.store.data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.mamykin.foboreader.store.data.network.BooksStoreService
import ru.mamykin.foboreader.store.domain.model.StoreBook
import ru.mamykin.foboreader.store.domain.model.StoreBookCategory
import javax.inject.Inject

class BooksStoreRepository @Inject constructor(
    private val service: BooksStoreService
) {
    private var allBooks: List<StoreBook>? = null
    private var books: List<StoreBook> = emptyList()
    private val loadBooksMutex = Mutex()

    suspend fun getCategories(): List<StoreBookCategory> {
        delay(1_000)
        return listOf(
            StoreBookCategory(
                id = "1",
                name = "Фантастика",
                description = null,
                booksCount = 4
            ),
            StoreBookCategory(
                id = "2",
                name = "Сказки",
                description = null,
                booksCount = 5
            ),
            StoreBookCategory(
                id = "1",
                name = "Учебная",
                description = null,
                booksCount = 2
            ),
        )
    }

    suspend fun newGetBooks(categoryId: String): List<StoreBook> {
        Log.e("this", "get books for category: $categoryId")
        return loadBooks()
    }

    @Deprecated("Use newGetBooks instead")
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