package ru.mamykin.store.domain

import ru.mamykin.store.data.BooksStoreRepository
import ru.mamykin.store.domain.entity.StoreBook

class BooksStoreInteractor constructor(
        private val repository: BooksStoreRepository
) {
    suspend fun getBooks(): List<StoreBook> {
        return repository.getBooks().featured.first().books
    }
}