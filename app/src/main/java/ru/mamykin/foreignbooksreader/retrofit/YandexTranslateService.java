package ru.mamykin.foreignbooksreader.retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.mamykin.foreignbooksreader.models.Translation;
import rx.Observable;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public interface YandexTranslateService {
    String BASE_URL = "https://translate.yandex.net/";

    @GET("api/v1.5/tr.json/translate")
    Observable<Translation> translate(
            @Query("key") String key,
            @Query("text") String text,
            @Query("lang") String lang,
            @Query("format") String format,
            @Query("options") String options
    );
}