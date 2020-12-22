package ru.mamykin.foboreader.my_books.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.model.SortOrder

@FlowPreview
@ExperimentalCoroutinesApi
class SortMyBooks(
    private val repository: MyBooksRepository
) {
    suspend operator fun invoke(sortOrder: SortOrder) {
        repository.sort(sortOrder)
    }
}