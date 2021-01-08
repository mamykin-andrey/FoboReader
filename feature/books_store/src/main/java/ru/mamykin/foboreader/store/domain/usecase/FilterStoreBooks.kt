package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import ru.mamykin.foboreader.store.data.BooksStoreRepository

class FilterStoreBooks(
    private val repository: BooksStoreRepository
) : UseCase<String, Unit>() {

    override fun execute(param: String) {
        repository.filter(param)
    }
}