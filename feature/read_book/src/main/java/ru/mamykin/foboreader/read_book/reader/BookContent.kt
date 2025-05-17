package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.read_book.translation.TextTranslation

internal data class BookContent(
    val sentences: List<TextTranslation>,
    val dictionary: Map<String, String> = emptyMap(),
)