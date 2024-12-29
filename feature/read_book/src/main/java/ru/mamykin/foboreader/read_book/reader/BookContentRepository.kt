package ru.mamykin.foboreader.read_book.reader

import javax.inject.Inject

internal class BookContentRepository @Inject constructor(
    private val bookContentParser: BookContentParser
) {
    suspend fun getBookContent(filePath: String): TranslatedText {
        return bookContentParser.parse(filePath)
    }
}