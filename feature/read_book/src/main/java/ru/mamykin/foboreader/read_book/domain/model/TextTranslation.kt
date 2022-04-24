package ru.mamykin.foboreader.read_book.domain.model

class TextTranslation(
    val sourceText: String,
    private val textTranslations: List<String>
) {
    fun getMostPreciseTranslation(): String? {
        return textTranslations.firstOrNull()
    }
}