package ru.mamykin.foboreader.read_book.reader

import javax.inject.Inject

@ReadBookScope
internal class BookContentRepository @Inject constructor(
    private val bookContentParser: BookContentParser
) {
    private lateinit var bookContent: BookContent

    suspend fun getBookContent(filePath: String): BookContent {
        return bookContentParser.parse(filePath)
            .also { bookContent = it }
    }

    fun getParagraphTranslation(sentence: String): String? {
        return bookContent.getTranslation(sentence)
    }
}