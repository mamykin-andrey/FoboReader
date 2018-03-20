package ru.mamykin.foreignbooksreader.retrofit

import retrofit2.http.GET
import ru.mamykin.foreignbooksreader.models.StoreBook
import rx.Observable

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
interface BooksStoreService {

    @get:GET("v2/592995eb11000014010828ee")
    val books: Observable<List<StoreBook>>

    companion object {
        val BASE_URL = "http://www.mocky.io/"
    }
}