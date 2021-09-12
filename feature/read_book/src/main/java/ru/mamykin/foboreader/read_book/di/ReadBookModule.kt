package ru.mamykin.foboreader.read_book.di

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDaoFactory
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.read_book.BuildConfig
import ru.mamykin.foboreader.read_book.data.network.GoogleTranslateService
import javax.inject.Named

@Module
class ReadBookModule {

    @Provides
    @ReadBookScope
    fun provideGoogleTranslateService(@CommonClient client: OkHttpClient): GoogleTranslateService =
        RetrofitServiceFactory.create(
            client,
            GoogleTranslateService.BASE_URL
        )

    @Named("ApiKey")
    @Provides
    @ReadBookScope
    fun provideGoogleApiKey() = BuildConfig.googleApiKey

    @Named("ApiHost")
    @Provides
    @ReadBookScope
    fun provideGoogleApiHost() = BuildConfig.googleApiHost

    @Provides
    @ReadBookScope
    fun provideBookInfoRepository(dao: BookInfoDao): BookInfoRepository = BookInfoRepository(dao)

    @Provides
    @ReadBookScope
    fun provideBookInfoDao(context: Context): BookInfoDao = BookInfoDaoFactory.create(context)
}