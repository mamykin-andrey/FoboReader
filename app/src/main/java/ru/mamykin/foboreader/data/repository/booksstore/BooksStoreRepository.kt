package ru.mamykin.foboreader.data.repository.booksstore

import ru.mamykin.foboreader.data.network.BooksStoreService
import ru.mamykin.foboreader.domain.entity.booksstore.BooksStoreResponse
import rx.Single
import javax.inject.Inject

class BooksStoreRepository @Inject constructor(
        private val booksService: BooksStoreService
) {
    fun getBooks(): Single<BooksStoreResponse> = booksService.getBooks()
}