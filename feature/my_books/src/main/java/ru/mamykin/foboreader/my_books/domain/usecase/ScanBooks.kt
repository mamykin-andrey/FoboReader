package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

class ScanBooks @Inject constructor(
    private val repository: MyBooksRepository
) : SuspendUseCase<Unit, Unit>() {

    override suspend fun execute(param: Unit) {
        repository.scanBooks()
    }
}