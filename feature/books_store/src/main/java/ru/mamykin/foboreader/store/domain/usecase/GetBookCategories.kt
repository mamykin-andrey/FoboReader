package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBookCategory
import javax.inject.Inject

class GetBookCategories @Inject constructor(
    private val repository: BooksStoreRepository
) : SuspendUseCase<Unit, List<StoreBookCategory>>() {

    override suspend fun execute(param: Unit): List<StoreBookCategory> {
        return repository.getCategories()
    }
}