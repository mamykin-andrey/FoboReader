package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.read_book.data.BookContentRepository
import ru.mamykin.foboreader.read_book.domain.model.BookContent

class GetBookContent(
    private val repository: BookContentRepository
) {
    suspend fun execute(filePath: String): BookContent {
        return repository.getBookContent(filePath)
    }
}