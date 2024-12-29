package ru.mamykin.foboreader.read_book.translation

import ru.mamykin.foboreader.read_book.reader.Book
import javax.inject.Inject

internal class GetParagraphTranslation @Inject constructor() {

    fun execute(book: Book, paragraph: String): String {
        return ""
        // TODO:
        // return requireNotNull(book.content.getTranslation(paragraph))
    }
}