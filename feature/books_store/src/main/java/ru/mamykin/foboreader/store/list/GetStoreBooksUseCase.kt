package ru.mamykin.foboreader.store.list

import ru.mamykin.foboreader.store.common.BooksStoreRepository
import javax.inject.Inject

internal class GetStoreBooksUseCase @Inject constructor(
    private val repository: BooksStoreRepository,
) {
    suspend fun execute(categoryId: String): Result<List<StoreBookEntity>> {
        return runCatching {
            repository.getBooks(categoryId)
        }
    }
}