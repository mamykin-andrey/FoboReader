package ru.mamykin.store.data

import ru.mamykin.store.domain.model.StoreBook

class BooksStoreRepository constructor(
        private val service: BooksStoreService
) {
    suspend fun getBooks(): List<StoreBook> {
        return service.getBooksAsync()
                .await()
                .books
                .map { it.toDomainModel() }
    }
}