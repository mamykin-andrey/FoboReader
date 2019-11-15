package ru.mamykin.foboreader.domain.booksstore

import ru.mamykin.foboreader.data.repository.booksstore.BooksStoreRepository
import ru.mamykin.foboreader.domain.entity.StoreBook
import javax.inject.Inject

class BooksStoreInteractor @Inject constructor(
        private val repository: BooksStoreRepository
) {
    suspend fun getBooks(): List<StoreBook> {
        return repository.getBooks().featured.first().books
    }
}