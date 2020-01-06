package ru.mamykin.store.data

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import ru.mamykin.store.data.model.StoreBooksModel

interface BooksStoreService {

    companion object {
        const val BASE_URL = "https://demo2817206.mockable.io"
    }

    @GET("ping")
    fun getBooksAsync(): Deferred<StoreBooksModel>
}