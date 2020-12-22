package ru.mamykin.foboreader.my_books.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import ru.mamykin.foboreader.my_books.data.MyBooksRepository

@FlowPreview
@ExperimentalCoroutinesApi
class FilterMyBooks(
    private val repository: MyBooksRepository
) {
    suspend operator fun invoke(query: String) {
        repository.filter(query)
    }
}