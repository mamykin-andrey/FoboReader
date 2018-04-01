package ru.mamykin.foboreader.data.repository.translate

import android.content.Context
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.Translation
import ru.mamykin.foboreader.data.network.YandexTranslateService
import rx.Single
import javax.inject.Inject

class TranslateRepository @Inject constructor(
        private val translateService: YandexTranslateService,
        private val context: Context
) {
    fun getTextTranslation(text: String): Single<Translation> {
        val apiKey = context.getString(R.string.yandex_api_key)
        return translateService.translate(apiKey, text, "ru", "", "")
    }
}