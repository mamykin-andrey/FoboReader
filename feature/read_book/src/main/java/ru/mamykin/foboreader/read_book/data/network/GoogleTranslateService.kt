package ru.mamykin.foboreader.read_book.data.network

import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.mamykin.foboreader.read_book.data.model.GoogleTranslateResponse

interface GoogleTranslateService {

    companion object {
        const val BASE_URL = "https://google-translate1.p.rapidapi.com/"
    }

    @POST("language/translate/v2")
    fun translateAsync(
        @Body body: RequestBody,
        @Header("content-type") contentType: String,
        @Header("accept-encoding") acceptEncoding: String,
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") apiHost: String
    ): Deferred<GoogleTranslateResponse>
}