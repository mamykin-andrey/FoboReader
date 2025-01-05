package ru.mamykin.foboreader.read_book.reader

internal interface BookContentParser {

    suspend fun parse(filePath: String): BookContent
}