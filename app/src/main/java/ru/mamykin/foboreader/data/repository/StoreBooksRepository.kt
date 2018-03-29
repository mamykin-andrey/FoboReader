package ru.mamykin.foboreader.data.repository

import ru.mamykin.foboreader.data.model.StoreBook
import ru.mamykin.foboreader.data.network.BooksStoreService
import rx.Single
import javax.inject.Inject

class StoreBooksRepository @Inject constructor(
        private val booksService: BooksStoreService
) {
    fun getBooks(): Single<List<StoreBook>> {
        return booksService.getBooks()
    }
}