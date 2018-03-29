package ru.mamykin.foboreader.domain.booksstore

import ru.mamykin.foboreader.data.model.StoreBook
import ru.mamykin.foboreader.data.repository.StoreBooksRepository
import rx.Single
import javax.inject.Inject

class BooksStoreInteractor @Inject constructor(
        private val repository: StoreBooksRepository
) {
    fun getStoreCategories(): Single<List<StoreBook>> {
        return repository.getStoreCategories()
    }
}