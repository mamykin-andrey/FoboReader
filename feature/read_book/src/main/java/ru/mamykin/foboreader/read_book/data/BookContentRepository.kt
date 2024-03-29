package ru.mamykin.foboreader.read_book.data

import ru.mamykin.foboreader.read_book.di.ReadBookScope
import ru.mamykin.foboreader.read_book.domain.helper.BookContentParser
import ru.mamykin.foboreader.read_book.domain.model.BookContent
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