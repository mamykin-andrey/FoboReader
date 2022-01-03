package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import javax.inject.Inject

class SortMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    fun execute(param: SortOrder): Result<Unit> {
        return runCatching {
            repository.sort(param)
        }
    }
}