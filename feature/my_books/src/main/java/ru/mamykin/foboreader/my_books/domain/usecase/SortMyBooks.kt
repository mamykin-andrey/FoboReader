package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.SuspendUseCase
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.model.SortOrder

class SortMyBooks(
    private val repository: MyBooksRepository
) : SuspendUseCase<SortOrder, Unit>() {

    override suspend fun execute(param: SortOrder) {
        repository.sort(param)
    }
}