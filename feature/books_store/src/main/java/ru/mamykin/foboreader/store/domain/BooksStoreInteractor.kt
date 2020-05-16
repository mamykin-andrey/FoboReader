package ru.mamykin.foboreader.store.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.mamykin.foboreader.core.network.FileDownloader
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook

@ExperimentalCoroutinesApi
@FlowPreview
class BooksStoreInteractor constructor(
        private val repository: BooksStoreRepository,
        private val fileDownloader: FileDownloader
) {
    private val booksChannel = BroadcastChannel<List<StoreBook>>(1)
    val booksFlow = booksChannel.asFlow()

    suspend fun loadBooks() {
        booksChannel.send(repository.getBooks())
    }

    suspend fun filterBooks(query: String) {
        booksChannel.send(repository.getBooks(query))
    }

    suspend fun downloadBook(book: StoreBook) {
        fileDownloader.download(book.link, book.getFileName())
    }
}