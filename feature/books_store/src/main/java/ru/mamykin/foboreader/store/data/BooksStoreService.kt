package ru.mamykin.foboreader.store.data

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import ru.mamykin.foboreader.store.data.model.StoreBooksModel

interface BooksStoreService {

    companion object {
        const val BASE_URL = "https://fobo-reader-backend.herokuapp.com"
    }

    @GET("books")
    fun getBooksAsync(): Deferred<StoreBooksModel>
}