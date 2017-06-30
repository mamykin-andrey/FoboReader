package ru.mamykin.foreignbooksreader.retrofit;

import java.util.List;

import retrofit2.http.GET;
import ru.mamykin.foreignbooksreader.models.StoreBook;
import rx.Observable;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
public interface BooksStoreService {
    String BASE_URL = "http://www.mocky.io/";

    @GET("v2/592995eb11000014010828ee")
    Observable<List<StoreBook>> getBooks();
}