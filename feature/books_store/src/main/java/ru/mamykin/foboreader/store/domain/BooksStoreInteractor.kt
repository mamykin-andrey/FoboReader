package ru.mamykin.foboreader.store.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import ru.mamykin.foboreader.store.data.network.FileDownloader
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook

@ExperimentalCoroutinesApi
@FlowPreview
class BooksStoreInteractor constructor(
    private val repository: BooksStoreRepository,
    private val fileDownloader: FileDownloader
) {
    suspend fun loadBooks(): List<StoreBook> {
        return repository.getBooks()
    }

    suspend fun filterBooks(query: String): List<StoreBook> {
        return repository.getBooks(query)
    }

    suspend fun downloadBook(book: StoreBook) {
        fileDownloader.download(book.link, book.getFileName())
    }
}