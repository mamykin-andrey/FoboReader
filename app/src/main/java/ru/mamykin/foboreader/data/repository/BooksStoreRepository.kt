package ru.mamykin.foboreader.data.repository

import ru.mamykin.foboreader.data.network.BooksStoreService
import ru.mamykin.foboreader.entity.StoreBook
import rx.Single
import javax.inject.Inject

class BooksStoreRepository @Inject constructor(
        private val booksService: BooksStoreService
) {
    fun getBooks(): Single<List<StoreBook>> = booksService.getBooks()
}