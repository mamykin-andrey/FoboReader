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
import ru.mamykin.foboreader.app.platform.PermissionManagerImpl
import ru.mamykin.foboreader.app.platform.ResourceManagerImpl
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDaoFactory
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.data.OkHttpFactory
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.NotificationManager
import ru.mamykin.foboreader.core.platform.PermissionManager
import ru.mamykin.foboreader.core.platform.ResourceManager

@Module
@InstallIn(SingletonComponent::class)
internal object AppProvidesModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpFactory.create(true)

    @Provides
    fun provideBookInfoRepository(dao: BookInfoDao): BookInfoRepository = BookInfoRepository(dao)

    @Provides
    fun provideBookInfoDao(@ApplicationContext context: Context): BookInfoDao = BookInfoDaoFactory.create(context)
}

@Module
@InstallIn(SingletonComponent::class)
internal interface AppBindsModule {

    @Binds
    fun bindResourceManager(impl: ResourceManagerImpl): ResourceManager

    @Binds
    fun bindNotificationManager(impl: NotificationManagerImpl): NotificationManager

    @Binds
    fun bindPreferencesManager(impl: PreferencesManagerImpl): PreferencesManager

    @Binds
    fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository

    @Binds
    fun bindErrorMapper(impl: ErrorMessageMapperImpl): ErrorMessageMapper

    @Binds
    fun bindPermissionManager(impl: PermissionManagerImpl): PermissionManager
}