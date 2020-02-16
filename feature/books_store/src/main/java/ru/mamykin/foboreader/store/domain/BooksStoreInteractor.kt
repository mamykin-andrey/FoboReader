package ru.mamykin.foboreader.store.domain

import ru.mamykin.foboreader.core.network.FileDownloader
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook

class BooksStoreInteractor constructor(
        private val repository: BooksStoreRepository,
        private val fileDownloader: FileDownloader
) {
    suspend fun getBooks(): List<StoreBook> {
        return repository.getBooks()
    }

    suspend fun downloadBook(book: StoreBook) {
        fileDownloader.download(book.link, book.getFileName())
    }
}