package ru.mamykin.foboreader.store.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mamykin.foboreader.core.domain.FlowUseCase
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook

class GetStoreBooks(
    private val repository: BooksStoreRepository
) : FlowUseCase<Unit, List<StoreBook>>() {

    override fun execute(): Flow<List<StoreBook>> {
        return repository.booksFlow
    }
}