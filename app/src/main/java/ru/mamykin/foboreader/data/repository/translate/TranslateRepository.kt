package ru.mamykin.foboreader.data.repository.translate

import android.content.Context
import ru.mamykin.foboreader.data.network.YandexTranslateService
import javax.inject.Inject

class TranslateRepository @Inject constructor(
        private val translateService: YandexTranslateService,
        context: Context
) {
    //    private val apiKey = context.getString(R.string.yandex_api_key)
    private val apiKey = ""

    suspend fun getTextTranslation(text: String): String {
        val translation = translateService.translate(apiKey, text, "ru", "", "").await()
        return translation.text.joinToString()
    }
}