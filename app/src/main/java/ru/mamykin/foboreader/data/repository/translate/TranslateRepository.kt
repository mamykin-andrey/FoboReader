package ru.mamykin.foboreader.data.repository.translate

import android.content.Context
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.data.network.YandexTranslateService
import ru.mamykin.foboreader.entity.Translation
import rx.Single
import javax.inject.Inject

class TranslateRepository @Inject constructor(
        private val translateService: YandexTranslateService,
        context: Context
) {
    private val apiKey = context.getString(R.string.yandex_api_key)

    fun getTextTranslation(text: String): Single<Translation> =
            translateService.translate(apiKey, text, "ru", "", "")
}