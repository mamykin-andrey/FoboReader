package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.BookCategory
import javax.inject.Inject

internal class GetBookCategories @Inject constructor(
    private val repository: BooksStoreRepository,
) {
    suspend fun execute(): Result<List<BookCategory>> {
        return runCatching {
            Result.success(repository.getCategories())
        }.getOrElse { Result.error(it) }
    }
}