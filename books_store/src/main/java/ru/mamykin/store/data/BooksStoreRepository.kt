package ru.mamykin.store.data

import ru.mamykin.store.data.model.BooksStoreResponse
import javax.inject.Inject

class BooksStoreRepository @Inject constructor(
        private val booksService: BooksStoreService
) {
    suspend fun getBooks(): BooksStoreResponse {
        return booksService.getBooksAsync().await()
    }
}