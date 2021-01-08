package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.read_book.data.BookContentRepository
import ru.mamykin.foboreader.read_book.domain.entity.BookContent

class GetBookContent(
    private val repository: BookContentRepository
) : SuspendUseCase<String, BookContent>() {

    override suspend fun execute(param: String): BookContent {
        return repository.getBookContent(param)
    }
}