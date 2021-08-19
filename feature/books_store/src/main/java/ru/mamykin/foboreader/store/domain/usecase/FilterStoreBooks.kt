package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook

class FilterStoreBooks(
    private val repository: BooksStoreRepository
) : SuspendUseCase<String, List<StoreBook>>() {

    override suspend fun execute(param: String): List<StoreBook> {
        return repository.getBooks(param)
    }
}