package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

class FilterMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) : UseCase<String, Unit>() {

    override fun execute(param: String) {
        repository.filter(param)
    }
}