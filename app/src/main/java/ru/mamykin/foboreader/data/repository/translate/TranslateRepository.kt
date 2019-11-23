package ru.mamykin.foboreader.data.repository.translate

import ru.mamykin.foboreader.data.network.YandexTranslateService
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