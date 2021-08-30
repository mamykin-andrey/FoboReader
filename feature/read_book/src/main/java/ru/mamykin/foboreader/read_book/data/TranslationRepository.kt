package ru.mamykin.foboreader.read_book.data

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.mamykin.foboreader.read_book.data.network.GoogleTranslateService
import ru.mamykin.foboreader.read_book.di.qualifier.GoogleTranslateApiHost
import ru.mamykin.foboreader.read_book.di.qualifier.GoogleTranslateApiKey
import ru.mamykin.foboreader.read_book.domain.entity.TranslationEntity
import javax.inject.Inject

class TranslationRepository @Inject constructor(
    private val service: GoogleTranslateService,
    @GoogleTranslateApiKey private val apiKey: String,
    @GoogleTranslateApiHost private val apiHost: String
) {
    suspend fun getTranslation(text: String): TranslationEntity {
        val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
        val body = "q=$text&source=en&target=ru".toRequestBody(mediaType)
        return service.translateAsync(
            body,
            "application/x-www-form-urlencoded",
            "application/gzip",
            apiKey,
            apiHost
        ).await()
            .toEntity(text)
    }
}