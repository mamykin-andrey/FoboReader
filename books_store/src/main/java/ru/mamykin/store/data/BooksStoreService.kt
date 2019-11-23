package ru.mamykin.store.data

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import ru.mamykin.store.data.model.BooksStoreResponse

interface BooksStoreService {

    companion object {
        const val BASE_URL = "http://www.mocky.io/"
    }

    @GET("v2/592995eb11000014010828ee")
    fun getBooksAsync(): Deferred<BooksStoreResponse>
}