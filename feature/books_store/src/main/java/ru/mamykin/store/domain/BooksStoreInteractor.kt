package ru.mamykin.store.domain

import ru.mamykin.store.data.BooksStoreRepository
import ru.mamykin.store.domain.model.StoreBook

class BooksStoreInteractor constructor(
        private val repository: BooksStoreRepository,
        private val fileDownloader: FileDownloader
) {
    suspend fun getBooks(): List<StoreBook> {
        return repository.getBooks()
    }

    suspend fun downloadBook(book: StoreBook) {
        fileDownloader.download(book.link, book.getBookName())
    }
}