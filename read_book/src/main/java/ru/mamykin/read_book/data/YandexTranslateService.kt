package ru.mamykin.read_book.data

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mamykin.read_book.domain.entity.Translation

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