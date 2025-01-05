package ru.mamykin.foboreader.store.common

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.store.categories.BookCategory
import ru.mamykin.foboreader.store.list.StoreBook
import ru.mamykin.foboreader.store.search.StoreSearchModel
import javax.inject.Inject

internal class BooksStoreRepository @Inject constructor(
    private val service: MockBooksStoreService,
    private val appSettingsRepository: AppSettingsRepository,
) {
    private val locale: String
        get() = appSettingsRepository.getAppLanguageCode()

    suspend fun getCategories(): List<BookCategory> {
        return service.getStoreCategories(locale)
            .categories
            .map { it.toDomainModel() }
    }

    suspend fun getBooks(categoryId: String): List<StoreBook> {
        return service.getStoreBooks(locale, categoryId)
            .books
            .map { it.toDomainModel() }
    }

    suspend fun search(searchQuery: String): StoreSearchModel {
        return service.searchInStore(locale, searchQuery).toDomainModel()
    }
}