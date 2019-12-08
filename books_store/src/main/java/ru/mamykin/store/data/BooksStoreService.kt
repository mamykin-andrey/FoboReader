package ru.mamykin.store.data

import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ru.mamykin.store.data.model.BooksStoreResponse

interface BooksStoreService {

    companion object {

        private const val BASE_URL = "http://www.mocky.io/"

        fun create(): BooksStoreService {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()

            return retrofit.create(BooksStoreService::class.java)
        }
    }

    @GET("v2/592995eb11000014010828ee")
    fun getBooksAsync(): Deferred<BooksStoreResponse>
}