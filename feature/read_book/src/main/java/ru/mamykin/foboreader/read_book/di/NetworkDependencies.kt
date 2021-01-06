package ru.mamykin.foboreader.read_book.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mamykin.foboreader.read_book.data.network.GoogleTranslateService

internal object NetworkDependencies {

    fun service(): GoogleTranslateService {
        val retrofit = Retrofit.Builder()
            .baseUrl(GoogleTranslateService.BASE_URL)
            .client(client())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(GoogleTranslateService::class.java)
    }

    private fun client(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }
}