package ru.mamykin.foboreader.read_book.domain.model

data class Translation(
    val source: String,
    val translations: List<String>
) {
    fun getMostLikelyTranslation(): String? {
        return translations.firstOrNull()
    }
}