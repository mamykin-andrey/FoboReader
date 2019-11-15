package ru.mamykin.foboreader.data.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mamykin.foboreader.domain.entity.Translation

interface YandexTranslateService {

    companion object {
        const val BASE_URL = "https://translate.yandex.net/"
    }

    @GET("api/v1.5/tr.json/translate")
    fun translate(
            @Query("key") key: String,
            @Query("text") text: String,
            @Query("lang") lang: String,
            @Query("format") format: String,
            @Query("options") options: String
    ): Deferred<Translation>
}