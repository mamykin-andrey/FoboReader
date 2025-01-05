package ru.mamykin.foboreader.read_book.reader

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory
import ru.mamykin.foboreader.read_book.BuildConfig
import ru.mamykin.foboreader.read_book.translation.GoogleTranslateService
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
internal class ReadBookProvidesModule {

    @Provides
    fun provideGoogleTranslateService(client: OkHttpClient): GoogleTranslateService =
        RetrofitServiceFactory.create(
            client,
            GoogleTranslateService.BASE_URL
        )

    @Named("ApiKey")
    @Provides
    fun provideGoogleApiKey() = BuildConfig.googleApiKey

    @Named("ApiHost")
    @Provides
    fun provideGoogleApiHost() = BuildConfig.googleApiHost
}

@Module
@InstallIn(ViewModelComponent::class)
internal interface ReadBookBindsModule {

    @Binds
    fun bindBookParser(parser: JsonBookContentParser): BookContentParser
}