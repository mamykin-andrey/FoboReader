package ru.mamykin.foboreader.app.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.app.data.AppSettingsRepositoryImpl
import ru.mamykin.foboreader.app.data.storage.PreferencesManagerImpl
import ru.mamykin.foboreader.app.platform.ErrorMessageMapperImpl
import ru.mamykin.foboreader.app.platform.NotificationManagerImpl
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDaoFactory
import ru.mamykin.foboreader.common_book_info.data.database.DownloadedBooksDao
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.data.OkHttpFactory
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.NotificationManager
import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.GetStreakInfoUseCase
import ru.mamykin.foboreader.dictionary_api.GetWordsToLearnUseCase
import ru.mamykin.foboreader.dictionary_api.UpdateStreakUseCase
import ru.mamykin.foboreader.dictionary_impl.DictionaryDaoFactory
import ru.mamykin.foboreader.dictionary_impl.GetStreakInfoUseCaseImpl
import ru.mamykin.foboreader.dictionary_impl.GetWordsToLearnUseCaseImpl
import ru.mamykin.foboreader.dictionary_impl.RoomDictionaryRepository
import ru.mamykin.foboreader.dictionary_impl.UpdateStreakUseCaseImpl
import ru.mamykin.foboreader.dictionary_impl.WordDictionaryDao

@Module
@InstallIn(SingletonComponent::class)
internal object AppProvidesModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpFactory.create(true)

    @Provides
    fun provideBookInfoDao(
        @ApplicationContext context: Context
    ): DownloadedBooksDao = BookInfoDaoFactory.create(context)

    @Provides
    fun provideDictionaryDao(
        @ApplicationContext context: Context
    ): WordDictionaryDao = DictionaryDaoFactory.create(context)
}

@Module
@InstallIn(SingletonComponent::class)
internal interface AppBindsModule {

    @Binds
    fun bindNotificationManager(impl: NotificationManagerImpl): NotificationManager

    @Binds
    fun bindPreferencesManager(impl: PreferencesManagerImpl): PreferencesManager

    @Binds
    fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository

    @Binds
    fun bindErrorMapper(impl: ErrorMessageMapperImpl): ErrorMessageMapper

    @Binds
    fun bindDictionaryRepository(impl: RoomDictionaryRepository): DictionaryRepository

    @Binds
    fun bindGetStreakInfoUseCase(impl: GetStreakInfoUseCaseImpl): GetStreakInfoUseCase

    @Binds
    fun bindUpdateStreakUseCase(impl: UpdateStreakUseCaseImpl): UpdateStreakUseCase

    @Binds
    fun bindGetWordsToLearnUseCase(impl: GetWordsToLearnUseCaseImpl): GetWordsToLearnUseCase
}