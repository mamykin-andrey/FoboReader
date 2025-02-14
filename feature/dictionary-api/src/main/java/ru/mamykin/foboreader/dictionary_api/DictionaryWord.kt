package ru.mamykin.foboreader.dictionary_api

import java.util.Date

data class DictionaryWord(
    val id: Long,
    val word: String,
    val translation: String,
    val dateAdded: Date,
)