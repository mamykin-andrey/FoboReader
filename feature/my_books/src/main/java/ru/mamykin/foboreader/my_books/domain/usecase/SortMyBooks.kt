package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import javax.inject.Inject

class SortMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) : UseCase<SortOrder, Unit>() {

    override fun execute(param: SortOrder) {
        repository.sort(param)
    }
}