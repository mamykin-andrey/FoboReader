package ru.mamykin.store.di

import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mamykin.store.data.BooksStoreService
import javax.inject.Singleton

class BooksStoreModule {

    @Provides
    @Singleton
    fun provideBooksStoreService(): BooksStoreService {
        val retrofit = Retrofit.Builder()
                .baseUrl(BooksStoreService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(BooksStoreService::class.java)
    }
}