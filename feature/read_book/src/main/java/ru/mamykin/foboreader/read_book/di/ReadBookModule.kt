package ru.mamykin.foboreader.read_book.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.read_book.BuildConfig
import ru.mamykin.foboreader.read_book.data.network.GoogleTranslateService
import ru.mamykin.foboreader.read_book.di.qualifier.GoogleTranslateApiHost
import ru.mamykin.foboreader.read_book.di.qualifier.GoogleTranslateApiKey

@Module
class ReadBookModule {

    @Provides
    fun provideGoogleTranslateService(@CommonClient client: OkHttpClient): GoogleTranslateService =
        RetrofitServiceFactory.create(
            client,
            GoogleTranslateService.BASE_URL
        )

    @GoogleTranslateApiKey
    @Provides
    fun provideGoogleApiKey() = BuildConfig.googleApiKey

    @GoogleTranslateApiHost
    @Provides
    fun provideGoogleApiHost() = BuildConfig.googleApiHost
}