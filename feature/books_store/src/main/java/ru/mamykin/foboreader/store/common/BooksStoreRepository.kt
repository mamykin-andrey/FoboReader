package ru.mamykin.foboreader.store.common

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.mamykin.foboreader.common_book_info.data.repository.DownloadedBooksRepository
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.store.categories.BookCategoryEntity
import ru.mamykin.foboreader.store.list.StoreBookEntity
import ru.mamykin.foboreader.store.search.SearchResultsEntity
import javax.inject.Inject

internal class BooksStoreRepository @Inject constructor(
    private val service: MockBooksStoreService,
    private val appSettingsRepository: AppSettingsRepository,
    private val downloadedBooksRepository: DownloadedBooksRepository,
) {
    private val locale: String
        get() = appSettingsRepository.getAppLanguageCode()

    suspend fun getCategories(): List<BookCategoryEntity> {
        return service.getStoreCategories(locale)
            .categories
            .map { it.toDomainModel() }
    }

    suspend fun getBooks(categoryId: String): List<StoreBookEntity> = coroutineScope {
        val downloadedBooksAsync = async { downloadedBooksRepository.getBooks() }
        val storeBooks = service.getStoreBooks(locale, categoryId).books
        val downloadedBooksLinks = downloadedBooksAsync.await().map { it.link }
        return@coroutineScope storeBooks.map {
            it.toDomainModel(downloadedBooksLinks.contains(it.link))
        }
    }

    suspend fun search(searchQuery: String): SearchResultsEntity = coroutineScope {
        val downloadedBooksAsync = async { downloadedBooksRepository.getBooks() }
        val searchResults = service.searchInStore(locale, searchQuery)
        val downloadedBooksLinks = downloadedBooksAsync.await().map { it.link }
        return@coroutineScope SearchResultsEntity(
            searchResults.categories.map { it.toDomainModel() },
            searchResults.books.map { it.toDomainModel(downloadedBooksLinks.contains(it.link)) }
        )
    }
}