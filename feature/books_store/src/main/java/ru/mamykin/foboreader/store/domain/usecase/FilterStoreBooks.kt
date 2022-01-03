package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook
import javax.inject.Inject

internal class FilterStoreBooks @Inject constructor(
    private val repository: BooksStoreRepository
) {
    suspend fun execute(categoryId: String, searchQuery: String): Result<List<StoreBook>> {
        return runCatching {
            Result.success(repository.getBooks(categoryId, searchQuery))
        }.getOrElse { Result.error(it) }
    }
}