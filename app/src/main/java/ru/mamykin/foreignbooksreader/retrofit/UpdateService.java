package ru.mamykin.foreignbooksreader.retrofit;

import retrofit2.http.GET;
import ru.mamykin.foreignbooksreader.models.UpdateInfo;
import rx.Observable;

/**
 * Creation date: 5/29/2017
 * Creation time: 3:30 PM
 * @author Andrey Mamykin(mamykin_av)
 */
public interface UpdateService {
    String BASE_URL = "http://www.mocky.io/";

    @GET("v2/592bfedb100000b414389889")
    Observable<UpdateInfo> getChangelog();
}