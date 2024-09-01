package ru.mamykin.foboreader.read_book.translation

import androidx.annotation.Keep

@Keep
internal class GoogleTranslateResponse(
    val data: DataResponse
) {
    class DataResponse(
        val translations: List<TranslationResponse>
    )

    class TranslationResponse(
        val translatedText: String
    )

    fun toDomainModel(sourceText: String) = TextTranslation(
        sourceText,
        data.translations.map { it.translatedText }
    )
}