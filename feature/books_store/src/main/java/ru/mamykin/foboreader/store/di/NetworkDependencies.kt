package ru.mamykin.foboreader.store.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mamykin.foboreader.store.data.network.BooksStoreService

internal object NetworkDependencies {

    fun service(client: OkHttpClient): BooksStoreService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BooksStoreService.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(BooksStoreService::class.java)
    }

    fun client(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }
}