package ru.mamykin.foboreader.read_book.data.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mamykin.foboreader.read_book.data.model.Translation

interface YandexTranslateService {

    companion object {
        const val BASE_URL = "https://translate.yandex.net/"
    }

    @GET("api/v1.5/tr.json/translate")
    fun translateAsync(
            @Query("key") key: String,
            @Query("text") text: String,
            @Query("lang") lang: String,
            @Query("format") format: String,
            @Query("options") options: String
    ): Deferred<Translation>
}