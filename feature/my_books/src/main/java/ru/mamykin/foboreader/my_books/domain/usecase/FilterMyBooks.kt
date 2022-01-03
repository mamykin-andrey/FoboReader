package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

class FilterMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    fun execute(searchQuery: String): Result<Unit> {
        return runCatching {
            Result.success(repository.filter(searchQuery))
        }.getOrElse { Result.error(it) }
    }
}