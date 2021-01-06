package ru.mamykin.foboreader.read_book.data.model

import com.google.gson.annotations.SerializedName
import ru.mamykin.foboreader.read_book.domain.entity.TranslationEntity

data class GoogleTranslateResponse(
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("translations")
        val translations: List<Translation>
    )
    data class Translation(
        @SerializedName("translatedText")
        val translatedText: String
    )

    fun toEntity(source: String) = TranslationEntity(
        source,
        data.translations.map { it.translatedText }
    )
}