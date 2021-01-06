package ru.mamykin.foboreader.read_book.domain.entity

data class TranslationEntity(
    val source: String,
    val translations: List<String>
) {
    fun getMostLikelyTranslation(): String? {
        return translations.firstOrNull()
    }
}