package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.read_book.data.BookContentRepository
import ru.mamykin.foboreader.read_book.domain.entity.BookContent
import javax.inject.Inject

class GetBookContent @Inject constructor(
    private val repository: BookContentRepository
) {
    suspend fun execute(filePath: String): Result<BookContent> {
        return runCatching {
            repository.getBookContent(filePath)
        }
    }
}