package ru.mamykin.foboreader.data.network

import retrofit2.http.GET
import ru.mamykin.foboreader.entity.StoreBook
import rx.Single

interface BooksStoreService {

    companion object {
        const val BASE_URL = "http://www.mocky.io/"
    }

    @GET("v2/592995eb11000014010828ee")
    fun getBooks(): Single<List<StoreBook>>
}