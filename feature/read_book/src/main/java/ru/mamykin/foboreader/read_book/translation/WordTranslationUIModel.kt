package ru.mamykin.foboreader.read_book.translation

internal data class WordTranslationUIModel(
    val word: String,
    val translation: String,
    val isInDictionary: Boolean,
) {
    fun fromDomainModel(entity: TextTranslation, isInDictionary: Boolean) = WordTranslationUIModel(
        word = entity.sourceText,
        translation = entity.getMostPreciseTranslation() ?: "",
        isInDictionary = isInDictionary,
    )
}