package ru.mamykin.foboreader.read_book.data.model

import androidx.annotation.Keep
import ru.mamykin.foboreader.read_book.domain.entity.TranslationEntity

@Keep
class GoogleTranslateResponse(
    val data: Data
) {
    class Data(
        val translations: List<Translation>
    )

    class Translation(
        val translatedText: String
    )

    fun toEntity(source: String) = TranslationEntity(
        source,
        data.translations.map { it.translatedText }
    )
}