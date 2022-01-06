package ru.mamykin.foboreader.store.data

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.store.data.network.TestBooksStoreService
import ru.mamykin.foboreader.store.domain.model.BookCategory
import ru.mamykin.foboreader.store.domain.model.StoreBook
import javax.inject.Inject

internal class BooksStoreRepository @Inject constructor(
    private val service: TestBooksStoreService,
    private val appSettingsStorage: AppSettingsStorage,
) {
    private var categoryBooks: List<StoreBook>? = null
    private val locale: String
        get() = appSettingsStorage.appLanguageCode

    suspend fun getCategories(): List<BookCategory> {
        return service.getCategories(locale)
            .categories
            .map { it.toDomainModel() }
    }

    suspend fun getBooks(
        categoryId: String,
        searchQuery: String? = null,
    ): List<StoreBook> {
        val categoryBooks = this.categoryBooks
            ?: getBooksRemote(categoryId).also { categoryBooks = it }
        return filterBooks(categoryBooks, searchQuery)
    }

    private suspend fun getBooksRemote(
        categoryId: String,
    ): List<StoreBook> {
        return service.getBooks(locale, categoryId)
            .books
            .map { it.toDomainModel() }
    }

    private fun filterBooks(
        books: List<StoreBook>,
        searchQuery: String?
    ): List<StoreBook> {
        if (searchQuery.isNullOrBlank()) {
            return books
        }
        return books.filter { it.containsText(searchQuery) }
    }
}