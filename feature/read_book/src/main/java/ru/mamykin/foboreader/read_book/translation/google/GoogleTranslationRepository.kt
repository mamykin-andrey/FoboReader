package ru.mamykin.foboreader.read_book.translation.google

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.mamykin.foboreader.read_book.translation.TextTranslation
import ru.mamykin.foboreader.read_book.translation.TranslationRepository
import javax.inject.Inject
import javax.inject.Named

internal class GoogleTranslationRepository @Inject constructor(
    private val service: GoogleTranslateService,
    @Named("ApiKey") private val apiKey: String,
    @Named("ApiHost") private val apiHost: String
) : TranslationRepository {

    override suspend fun getTranslation(text: String): Result<TextTranslation> = runCatching {
        val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
        val body = "q=$text&source=en&target=ru".toRequestBody(mediaType)
        service.translateAsync(
            body,
            "application/x-www-form-urlencoded",
            "application/gzip",
            apiKey,
            apiHost
        ).await().let {
            val mostPreciseTranslation = it.data.translations.firstOrNull()?.translatedText ?: run {
                // TODO: Add crash report
                ""
            }
            TextTranslation(
                text = text,
                translation = mostPreciseTranslation,
            )
        }
    }
}