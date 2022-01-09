package ru.mamykin.foboreader.read_book.domain.model

class Translation(
    val sourceText: String,
    private val translations: List<String>
) {
    fun getMostPreciseTranslation(): String? {
        return translations.firstOrNull()
    }
}