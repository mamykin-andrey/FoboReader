package ru.mamykin.foboreader.read_book.data

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.mamykin.foboreader.read_book.data.network.GoogleTranslateService
import ru.mamykin.foboreader.read_book.domain.model.Translation
import javax.inject.Inject
import javax.inject.Named

internal class TranslationRepository @Inject constructor(
    private val service: GoogleTranslateService,
    @Named("ApiKey") private val apiKey: String,
    @Named("ApiHost") private val apiHost: String
) {
    suspend fun getTranslation(text: String): Translation {
        val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
        val body = "q=$text&source=en&target=ru".toRequestBody(mediaType)
        return service.translateAsync(
            body,
            "application/x-www-form-urlencoded",
            "application/gzip",
            apiKey,
            apiHost
        ).await()
            .toDomainModel(text)
    }
}