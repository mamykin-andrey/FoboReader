package ru.mamykin.read_book.data

import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mamykin.read_book.domain.entity.Translation

interface YandexTranslateService {

    companion object {

        private const val BASE_URL = "https://translate.yandex.net/"

        fun create(): YandexTranslateService {

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()

            return retrofit.create(YandexTranslateService::class.java)
        }
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