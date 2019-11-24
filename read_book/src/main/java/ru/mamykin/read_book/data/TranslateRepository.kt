package ru.mamykin.read_book.data

import javax.inject.Inject

class TranslateRepository @Inject constructor(
        private val translateService: YandexTranslateService
) {
    private val apiKey = ""

    suspend fun getTextTranslation(text: String): String {
        val translation = translateService.translateAsync(apiKey, text, "ru", "", "").await()
        return translation.text.joinToString()
    }
}