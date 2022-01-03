package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

class ScanBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    suspend fun execute(): Result<Unit> {
        return runCatching {
            Result.success(repository.scanBooks())
        }.getOrElse { Result.error(it) }
    }
}