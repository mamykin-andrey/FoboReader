package ru.mamykin.foboreader.read_book.translation

internal data class WordTranslationUIModel(
    val word: String,
    val translation: String,
    val dictionaryId: Long?,
) {
    companion object {

        fun fromDomainModel(entity: WordTranslation) = WordTranslationUIModel(
            word = entity.word,
            translation = entity.translation,
            dictionaryId = entity.dictionaryId,
        )
    }
}