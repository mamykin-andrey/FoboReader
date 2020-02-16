package ru.mamykin.foboreader.store.domain.model

import ru.mamykin.foboreader.core.extension.transliterate

data class StoreBook(
        val genre: String,
        val author: String,
        val title: String,
        val lang: String,
        val format: String,
        val cover: String,
        val link: String
) {
    fun getFileName(): String = "${title.transliterate()}.$format"
}