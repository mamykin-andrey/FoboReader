package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.read_book.data.BookContentRepository
import javax.inject.Inject

class GetParagraphTranslation @Inject constructor(
    private val repository: BookContentRepository
) {
    fun execute(paragraph: String): Result<String?> {
        return runCatching {
            Result.success(repository.getParagraphTranslation(paragraph))
        }.getOrElse { Result.error(it) }
    }
}