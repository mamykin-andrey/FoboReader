package ru.mamykin.foboreader.data.repository.booksstore

import ru.mamykin.foboreader.data.network.BooksStoreService
import ru.mamykin.foboreader.domain.entity.booksstore.BooksStoreResponse
import javax.inject.Inject

class BooksStoreRepository @Inject constructor(
        private val booksService: BooksStoreService
) {
    suspend fun getBooks(): BooksStoreResponse {
        return booksService.getBooks()
    }
}