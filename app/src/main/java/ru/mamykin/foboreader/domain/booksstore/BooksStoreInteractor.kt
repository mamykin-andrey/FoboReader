package ru.mamykin.foboreader.domain.booksstore

import ru.mamykin.foboreader.entity.StoreBook
import ru.mamykin.foboreader.data.repository.booksstore.BooksStoreRepository
import rx.Single
import javax.inject.Inject

class BooksStoreInteractor @Inject constructor(
        private val repository: BooksStoreRepository
) {
    fun getBooks(): Single<List<StoreBook>> {
        return repository.getBooks()
    }
}