package ru.mamykin.foboreader.domain.booksstore

import ru.mamykin.foboreader.data.repository.booksstore.BooksStoreRepository
import ru.mamykin.foboreader.entity.booksstore.BooksStoreResponse
import rx.Single
import javax.inject.Inject

class BooksStoreInteractor @Inject constructor(
        private val repository: BooksStoreRepository
) {
    fun getBooks(): Single<BooksStoreResponse> = repository.getBooks()
}