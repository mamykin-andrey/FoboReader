package ru.mamykin.foboreader.read_book.data.model

import androidx.annotation.Keep
import ru.mamykin.foboreader.read_book.domain.model.Translation

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

    fun toDomainModel(source: String) = Translation(
        source,
        data.translations.map { it.translatedText }
    )
}