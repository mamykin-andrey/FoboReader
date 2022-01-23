package ru.mamykin.foboreader.app.di

import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import ru.mamykin.foboreader.app.data.storage.AppSettingsStorageImpl
import ru.mamykin.foboreader.app.data.storage.PreferencesManagerImpl
import ru.mamykin.foboreader.app.platform.NotificationManagerImpl
import ru.mamykin.foboreader.app.platform.ResourceManagerImpl
import ru.mamykin.foboreader.app.platform.VibratorHelperImpl
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.platform.NotificationManager
import ru.mamykin.foboreader.core.platform.ResourceManager
import ru.mamykin.foboreader.core.platform.VibratorHelper
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreCommonBindsModule::class])
internal interface CoreCommonComponent : CommonApi {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): CoreCommonComponent
    }
}

@Module
internal interface CoreCommonBindsModule {

    @Binds
    @Singleton
    fun bindResourceManager(impl: ResourceManagerImpl): ResourceManager

    @Binds
    @Singleton
    fun bindNotificationManager(impl: NotificationManagerImpl): NotificationManager

    @Binds
    @Singleton
    fun bindVibratorHelper(impl: VibratorHelperImpl): VibratorHelper

    @Binds
    @Singleton
    fun bindPreferencesManager(impl: PreferencesManagerImpl): PreferencesManager

    @Binds
    @Singleton
    fun bindAppSettings(impl: AppSettingsStorageImpl): AppSettingsStorage
}