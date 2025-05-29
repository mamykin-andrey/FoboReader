package ru.mamykin.foboreader.learn_new_words

internal data class WordCard(
    val word: String,
    val translation: String,
    val shownCount: Int = 0,
)