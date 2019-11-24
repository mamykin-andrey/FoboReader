package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mamykin.store.data.BooksStoreService
import ru.mamykin.read_book.data.YandexTranslateService
import javax.inject.Singleton

@Module
@Singleton
class NetworkModule {

    @Provides
    @Singleton
    fun provideYandexTranslateService(): YandexTranslateService {
        val retrofit = Retrofit.Builder()
                .baseUrl(YandexTranslateService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(YandexTranslateService::class.java)
    }

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