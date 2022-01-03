package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.read_book.data.BookContentRepository
import javax.inject.Inject

class GetParagraphTranslation @Inject constructor(
    private val repository: BookContentRepository
) {
    fun execute(paragraph: String): String? {
        return repository.getParagraphTranslation(paragraph)
    }
}