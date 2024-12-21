package ru.mamykin.foboreader.store.main

import ru.mamykin.foboreader.store.common.BooksStoreRepository
import javax.inject.Inject

internal class GetBookCategories @Inject constructor(
    private val repository: BooksStoreRepository,
) {
    suspend fun execute(): Result<List<BookCategory>> {
        return runCatching {
            repository.getCategories()
        }
    }
}