package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook
import javax.inject.Inject

internal class GetStoreBooks @Inject constructor(
    private val repository: BooksStoreRepository,
) {
    suspend fun execute(categoryId: String): Result<List<StoreBook>> {
        return runCatching {
            repository.getBooks(categoryId)
        }
    }
}