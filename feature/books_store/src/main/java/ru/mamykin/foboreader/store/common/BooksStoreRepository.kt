package ru.mamykin.foboreader.store.common

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.store.categories.BookCategory
import ru.mamykin.foboreader.store.list.StoreBook
import javax.inject.Inject

internal class BooksStoreRepository @Inject constructor(
    private val service: MockBooksStoreService,
    private val appSettingsRepository: AppSettingsRepository,
) {
    private val locale: String
        get() = appSettingsRepository.getAppLanguageCode()

    suspend fun getCategories(): List<BookCategory> {
        return service.getCategories(locale)
            .categories
            .map { it.toDomainModel() }
    }

    suspend fun getBooks(
        categoryId: String,
        searchQuery: String? = null,
    ): List<StoreBook> {
        return service.getBooks(locale, categoryId, searchQuery)
            .books
            .map { it.toDomainModel() }
    }
}