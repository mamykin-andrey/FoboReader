package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

class FilterMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    fun execute(searchQuery: String) {
        repository.filter(searchQuery)
    }
}