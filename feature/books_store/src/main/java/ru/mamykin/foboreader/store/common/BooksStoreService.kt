package ru.mamykin.foboreader.store.common

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import ru.mamykin.foboreader.store.list.BookListResponse

internal interface BooksStoreService {

    companion object {
        const val BASE_URL = "https://fobo-reader-backend.herokuapp.com"
    }

    @GET("books")
    fun getBooksAsync(): Deferred<BookListResponse>
}