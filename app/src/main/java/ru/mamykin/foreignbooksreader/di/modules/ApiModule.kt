package ru.mamykin.foreignbooksreader.di.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mamykin.foreignbooksreader.data.network.BooksStoreService
import ru.mamykin.foreignbooksreader.data.network.UpdateService
import ru.mamykin.foreignbooksreader.data.network.YandexTranslateService

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
class ApiModule {
    @Provides
    fun provideYandexTranslateService(): YandexTranslateService {
        val retrofit = Retrofit.Builder()
                .baseUrl(YandexTranslateService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(YandexTranslateService::class.java!!)
    }

    @Provides
    fun provideBooksStoreService(): BooksStoreService {
        val retrofit = Retrofit.Builder()
                .baseUrl(BooksStoreService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(BooksStoreService::class.java!!)
    }

    @Provides
    fun provideUpdateService(): UpdateService {
        val retrofit = Retrofit.Builder()
                .baseUrl(UpdateService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(UpdateService::class.java!!)
    }
}