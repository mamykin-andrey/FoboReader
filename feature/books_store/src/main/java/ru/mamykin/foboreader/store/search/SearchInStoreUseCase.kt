package ru.mamykin.foboreader.store.search

import ru.mamykin.foboreader.core.extension.runCatchingCancellable
import ru.mamykin.foboreader.store.common.BooksStoreRepository
import javax.inject.Inject

internal class SearchInStoreUseCase @Inject constructor(
    private val repository: BooksStoreRepository,
) {
    suspend fun execute(searchQuery: String): Result<SearchResultsEntity> {
        return runCatchingCancellable {
            repository.search(searchQuery)
        }
    }
}