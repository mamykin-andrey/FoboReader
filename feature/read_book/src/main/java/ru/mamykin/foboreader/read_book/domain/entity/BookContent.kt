package ru.mamykin.foboreader.read_book.domain.entity

data class BookContent(
        val text: String,
        val translations: HashMap<String, String>?
) {
    fun getTranslation(paragraph: String): String? = translations?.let { it[paragraph] }
}