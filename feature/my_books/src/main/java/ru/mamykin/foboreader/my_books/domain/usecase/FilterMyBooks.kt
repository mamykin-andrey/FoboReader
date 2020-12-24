package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.SuspendUseCase
import ru.mamykin.foboreader.my_books.data.MyBooksRepository

class FilterMyBooks(
    private val repository: MyBooksRepository
) : SuspendUseCase<String, Unit>() {

    override suspend fun execute(param: String) {
        repository.filter(param)
    }
}