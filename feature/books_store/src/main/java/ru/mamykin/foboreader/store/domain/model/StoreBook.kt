package ru.mamykin.foboreader.store.domain.model

import ru.mamykin.foboreader.core.extension.StringTransliterator

data class StoreBook(
    val id: String,
    val genre: String,
    val author: String,
    val title: String,
    val lang: String,
    val format: String,
    val cover: String,
    val link: String
) {
    fun getFileName(): String {
        val translitName = StringTransliterator.transliterate(title)
        return "$translitName.$format"
    }

    fun containsText(text: String): Boolean {
        return title.contains(text, ignoreCase = true) || author.contains(text, ignoreCase = true)
    }
}