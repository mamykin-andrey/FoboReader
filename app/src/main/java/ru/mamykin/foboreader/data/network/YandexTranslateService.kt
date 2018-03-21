package ru.mamykin.foboreader.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.mamykin.foboreader.data.model.Translation
import rx.Observable

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
interface YandexTranslateService {

    @GET("api/v1.5/tr.json/translate")
    fun translate(
            @Query("key") key: String,
            @Query("text") text: String,
            @Query("lang") lang: String,
            @Query("format") format: String,
            @Query("options") options: String
    ): Observable<Translation>

    companion object {
        val BASE_URL = "https://translate.yandex.net/"
    }
}