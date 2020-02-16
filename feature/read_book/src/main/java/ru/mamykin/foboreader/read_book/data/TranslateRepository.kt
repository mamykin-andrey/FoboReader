package ru.mamykin.foboreader.read_book.data

class TranslateRepository constructor(
        private val translateService: YandexTranslateService
) {
    private val apiKey = ""

    suspend fun getTextTranslation(text: String): String {
        val translation = translateService.translateAsync(apiKey, text, "ru", "", "").await()
        return translation.text.joinToString()
    }
}