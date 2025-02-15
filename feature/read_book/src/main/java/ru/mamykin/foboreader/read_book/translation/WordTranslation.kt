package ru.mamykin.foboreader.read_book.translation

internal data class WordTranslation(
    val word: String,
    val translation: String,
    val dictionaryId: Long?,
)