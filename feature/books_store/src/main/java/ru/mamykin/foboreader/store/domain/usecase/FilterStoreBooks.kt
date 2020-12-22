package ru.mamykin.foboreader.store.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import ru.mamykin.foboreader.store.data.BooksStoreRepository

@ExperimentalCoroutinesApi
@FlowPreview
class FilterStoreBooks(
    private val repository: BooksStoreRepository
) {
    fun execute(query: String) {
        return repository.filter(query)
    }
}