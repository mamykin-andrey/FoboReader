package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.model.StoreBook
import javax.inject.Inject

internal class FilterStoreBooks @Inject constructor(
    private val repository: BooksStoreRepository
) : SuspendUseCase<Pair<String, String>, List<StoreBook>>() {

    override suspend fun execute(param: Pair<String, String>): List<StoreBook> {
        val (categoryId, searchQuery) = param
        return repository.getBooks(categoryId, searchQuery)
    }
}