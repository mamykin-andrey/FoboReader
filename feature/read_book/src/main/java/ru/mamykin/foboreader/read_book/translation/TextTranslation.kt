package ru.mamykin.foboreader.read_book.translation

class TextTranslation(
    val sourceText: String,
    private val textTranslations: List<String>
) {
    fun getMostPreciseTranslation(): String? {
        return textTranslations.firstOrNull()
    }
}