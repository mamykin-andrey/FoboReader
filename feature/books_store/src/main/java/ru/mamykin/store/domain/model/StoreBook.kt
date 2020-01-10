package ru.mamykin.store.domain.model

import java.util.*

data class StoreBook(
        val genre: String,
        val author: String,
        val title: String,
        val lang: String,
        private val _format: String,
        val cover: String,
        val link: String
) {
    fun getBookName(): String = "${title.hashCode()}.$_format"

    fun getFormat(): Format = when (_format.toLowerCase(Locale.getDefault())) {
        "fb2" -> Format.Fb2
        "fbwt" -> Format.Fbwt
        else -> Format.Unknown
    }

    sealed class Format {
        object Fb2 : Format()
        object Fbwt : Format()
        object Unknown : Format()
    }
}