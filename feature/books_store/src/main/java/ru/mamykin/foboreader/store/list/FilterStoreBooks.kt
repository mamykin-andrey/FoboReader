package ru.mamykin.foboreader.store.list

import ru.mamykin.foboreader.store.common.BooksStoreRepository
import javax.inject.Inject

internal class FilterStoreBooks @Inject constructor(
    private val repository: BooksStoreRepository
) {
    suspend fun execute(categoryId: String, searchQuery: String): Result<List<StoreBook>> {
        return runCatching {
            repository.getBooks(categoryId, searchQuery)
        }
    }
}