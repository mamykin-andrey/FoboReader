package ru.mamykin.store.domain

import ru.mamykin.store.data.BooksStoreRepository
import ru.mamykin.store.domain.model.StoreBook

class BooksStoreInteractor constructor(
        private val repository: BooksStoreRepository
) {
    suspend fun getBooks(): List<StoreBook> {
        return repository.getBooks()
    }
}