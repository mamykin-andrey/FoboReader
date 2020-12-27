package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.core.domain.UseCase
import ru.mamykin.foboreader.my_books.data.MyBooksRepository

class FilterMyBooks(
    private val repository: MyBooksRepository
) : UseCase<String, Unit>() {

    override fun execute(param: String) {
        repository.filter(param)
    }
}