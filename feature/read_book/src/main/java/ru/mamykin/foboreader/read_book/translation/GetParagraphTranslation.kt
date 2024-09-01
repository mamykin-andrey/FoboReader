package ru.mamykin.foboreader.read_book.translation

import ru.mamykin.foboreader.read_book.reader.BookContentRepository
import javax.inject.Inject

internal class GetParagraphTranslation @Inject constructor(
    private val repository: BookContentRepository
) {
    fun execute(paragraph: String): String? {
        return repository.getParagraphTranslation(paragraph.trim())
    }
}