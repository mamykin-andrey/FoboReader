package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.my_books.data.MyBooksRepository

class ScanBooks(
    private val repository: MyBooksRepository
) : SuspendUseCase<Unit, Unit>() {

    override suspend fun execute(param: Unit) {
        repository.scanBooks()
    }
}