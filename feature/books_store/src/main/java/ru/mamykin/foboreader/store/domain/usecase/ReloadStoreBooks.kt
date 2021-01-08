package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.store.data.BooksStoreRepository

class ReloadStoreBooks(
    private val repository: BooksStoreRepository
) : SuspendUseCase<Unit, Unit>() {

    override suspend fun execute(param: Unit) {
        repository.loadBooks()
    }
}