package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.read_book.data.BookContentRepository

class GetParagraphTranslation(
    private val repository: BookContentRepository
) {
    fun execute(sentence: String): String? {
        return repository.getParagraphTranslation(sentence)
    }
}