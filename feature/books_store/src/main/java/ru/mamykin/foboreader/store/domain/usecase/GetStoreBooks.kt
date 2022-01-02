package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook
import javax.inject.Inject

class GetStoreBooks @Inject constructor(
    private val repository: BooksStoreRepository
) : SuspendUseCase<String, List<StoreBook>>() {

    override suspend fun execute(categoryId: String): List<StoreBook> {
        return repository.newGetBooks(categoryId)
    }
}