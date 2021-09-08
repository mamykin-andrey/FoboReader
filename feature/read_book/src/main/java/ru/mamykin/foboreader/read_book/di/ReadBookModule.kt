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
import ru.mamykin.foboreader.read_book.di.qualifier.GoogleTranslateApiHost
import ru.mamykin.foboreader.read_book.di.qualifier.GoogleTranslateApiKey

@Module
class ReadBookModule {

    @Provides
    @ReadBookScope
    fun provideGoogleTranslateService(@CommonClient client: OkHttpClient): GoogleTranslateService =
        RetrofitServiceFactory.create(
            client,
            GoogleTranslateService.BASE_URL
        )

    @GoogleTranslateApiKey
    @Provides
    @ReadBookScope
    fun provideGoogleApiKey() = BuildConfig.googleApiKey

    @GoogleTranslateApiHost
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