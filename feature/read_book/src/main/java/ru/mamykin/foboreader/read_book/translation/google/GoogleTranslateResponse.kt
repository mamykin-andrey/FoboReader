package ru.mamykin.foboreader.read_book.translation.google

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
}