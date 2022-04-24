package ru.mamykin.foboreader.read_book.data.model

import androidx.annotation.Keep
import ru.mamykin.foboreader.read_book.domain.model.TextTranslation

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