package ru.mamykin.foboreader.store.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook

@ExperimentalCoroutinesApi
@FlowPreview
class GetStoreBooks(
    private val repository: BooksStoreRepository
) {
    fun execute(): Flow<List<StoreBook>> {
        return repository.booksFlow
    }
}