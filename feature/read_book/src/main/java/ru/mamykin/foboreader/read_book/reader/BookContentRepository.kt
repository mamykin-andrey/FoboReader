package ru.mamykin.foboreader.read_book.reader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class BookContentRepository @Inject constructor(
    private val bookContentParser: BookContentParser
) {
    suspend fun getBookContent(filePath: String): BookContent = withContext(Dispatchers.Default) {
        return@withContext bookContentParser.parse(filePath)
    }
}