package ru.mamykin.foboreader.store.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.mamykin.foboreader.store.data.network.BooksStoreService
import ru.mamykin.foboreader.store.domain.model.StoreBook
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@FlowPreview
class BooksStoreRepository(
    private val service: BooksStoreService
) {
    private val booksChannel = ConflatedBroadcastChannel<List<StoreBook>>()
    val booksFlow = booksChannel.asFlow()

    private var allBooks: List<StoreBook>? = null
    private var books by Delegates.observable(emptyList<StoreBook>()) { _, _, new ->
        booksChannel.offer(new)
    }

    suspend fun loadBooks() {
        service.getBooksAsync()
            .await()
            .books
            .map { it.toDomainModel() }
            .also { allBooks = it }
            .also { books = it }
    }

    fun filter(query: String?) {
        query ?: return
        val allBooks = this.allBooks ?: return
        books = allBooks.filter {
            it.title.contains(query, ignoreCase = true)
                    || it.author.contains(query, ignoreCase = true)
        }
    }
}