package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.read_book.data.BookContentRepository
import javax.inject.Inject

internal class GetBookText @Inject constructor(
    private val repository: BookContentRepository
) {
    suspend fun execute(filePath: String): String {
        return repository.getBookContent(filePath)
            .text
    }
}