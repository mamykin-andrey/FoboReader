package ru.mamykin.read_book.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mamykin.read_book.data.YandexTranslateService

@Module
class ReadBookModule {

    @Provides
    fun provideYandexTranslateService(): YandexTranslateService {
        val retrofit = Retrofit.Builder()
                .baseUrl(YandexTranslateService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(YandexTranslateService::class.java)
    }
}