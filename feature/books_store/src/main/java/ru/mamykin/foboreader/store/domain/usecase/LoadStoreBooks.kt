package ru.mamykin.foboreader.store.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import ru.mamykin.foboreader.store.data.BooksStoreRepository

@ExperimentalCoroutinesApi
@FlowPreview
class LoadStoreBooks(
    private val repository: BooksStoreRepository
) {
    suspend fun execute() {
        return repository.loadBooks()
    }
}