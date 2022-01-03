package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

class ScanBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    suspend fun execute(): Result<Unit> {
        return runCatching {
            repository.scanBooks()
        }
    }
}