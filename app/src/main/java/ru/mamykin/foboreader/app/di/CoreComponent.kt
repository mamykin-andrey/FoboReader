package ru.mamykin.foboreader.app.di

import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import ru.mamykin.foboreader.app.data.storage.AppSettingsRepositoryImpl
import ru.mamykin.foboreader.app.data.storage.PreferencesManagerImpl
import ru.mamykin.foboreader.app.platform.ErrorMessageMapperImpl
import ru.mamykin.foboreader.app.platform.NotificationManagerImpl
import ru.mamykin.foboreader.app.platform.ResourceManagerImpl
import ru.mamykin.foboreader.core.data.storage.AppSettingsRepository
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.NotificationManager
import ru.mamykin.foboreader.core.platform.ResourceManager
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreBindsModule::class])
internal interface CoreComponent : CommonApi {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): CoreComponent
    }
}

@Module
internal interface CoreBindsModule {

    @Binds
    @Singleton
    fun bindResourceManager(impl: ResourceManagerImpl): ResourceManager

    @Binds
    @Singleton
    fun bindNotificationManager(impl: NotificationManagerImpl): NotificationManager

    @Binds
    @Singleton
    fun bindPreferencesManager(impl: PreferencesManagerImpl): PreferencesManager

    @Binds
    @Singleton
    fun bindAppSettings(impl: AppSettingsRepositoryImpl): AppSettingsRepository

    @Binds
    @Singleton
    fun bindErrorMapper(impl: ErrorMessageMapperImpl): ErrorMessageMapper
}