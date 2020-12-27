package ru.mamykin.foboreader.store.data

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.store.data.network.BooksStoreService
import ru.mamykin.foboreader.store.domain.model.StoreBook
import kotlin.properties.Delegates

class BooksStoreRepository(
    private val service: BooksStoreService
) {
    private val booksChannel = ConflatedBroadcastChannel<List<StoreBook>>()

    private var allBooks: List<StoreBook>? = null
    private var books by Delegates.observable(emptyList<StoreBook>()) { _, _, new ->
        booksChannel.offer(new)
    }

    fun getBooks(): Flow<List<StoreBook>> = flow {
        runCatching { loadBooks() }
        booksChannel.consumeEach {
            emit(it)
        }
    }

    suspend fun loadBooks() {
        service.getBooksAsync()
            .await()
            .books
            .map { it.toDomainModel() }
            .also { allBooks = it }
            .also { books = it }
    }

    fun filter(query: String) {
        val allBooks = this.allBooks ?: return
        books = allBooks.filter { it.containsText(query) }
    }
}