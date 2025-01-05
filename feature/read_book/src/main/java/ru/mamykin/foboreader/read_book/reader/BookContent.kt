package ru.mamykin.foboreader.read_book.reader

data class BookContent(
    val sentences: List<String>,
    val translations: List<String>,
    val dictionary: Map<String, String> = emptyMap(),
)