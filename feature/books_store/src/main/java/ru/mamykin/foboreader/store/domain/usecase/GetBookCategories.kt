package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.BookCategory
import javax.inject.Inject

internal class GetBookCategories @Inject constructor(
    private val repository: BooksStoreRepository,
) : SuspendUseCase<Unit, List<BookCategory>>() {

    override suspend fun execute(param: Unit): List<BookCategory> {
        return repository.getCategories()
    }
}