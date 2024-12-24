package ru.mamykin.foboreader.app.di

import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.app.ReaderApp
import ru.mamykin.foboreader.app.data.AppSettingsRepositoryImpl
import ru.mamykin.foboreader.app.data.storage.PreferencesManagerImpl
import ru.mamykin.foboreader.app.navigation.TabComposableProviderImpl
import ru.mamykin.foboreader.app.platform.ErrorMessageMapperImpl
import ru.mamykin.foboreader.app.platform.NotificationManagerImpl
import ru.mamykin.foboreader.app.platform.PermissionManagerImpl
import ru.mamykin.foboreader.app.platform.ResourceManagerImpl
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.data.OkHttpFactory
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.MainApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.NotificationManager
import ru.mamykin.foboreader.core.platform.PermissionManager
import ru.mamykin.foboreader.core.platform.ResourceManager
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
internal interface AppComponent : NetworkApi, SettingsApi, MainApi, CommonApi {

    fun inject(app: ReaderApp)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}

@Module(includes = [AppModule.BindsModule::class])
internal class AppModule {

    @Provides
    @Singleton
    @CommonClient
    fun provideCommonClient(): OkHttpClient = OkHttpFactory.create(true)

    @Module
    interface BindsModule {

        @Binds
        @Singleton
        fun commonApi(component: AppComponent): CommonApi

        @Binds
        @Singleton
        fun networkApi(component: AppComponent): NetworkApi

        @Binds
        @Singleton
        fun settingsApi(component: AppComponent): SettingsApi

        @Binds
        @Singleton
        fun bindFragmentProvider(impl: TabComposableProviderImpl): TabComposableProvider

        @Binds
        @Singleton
        fun bindPreferencesManager(impl: PreferencesManagerImpl): PreferencesManager

        @Binds
        @Singleton
        fun bindErrorMapper(impl: ErrorMessageMapperImpl): ErrorMessageMapper

        @Binds
        @Singleton
        fun bindResourceManager(impl: ResourceManagerImpl): ResourceManager

        @Binds
        @Singleton
        fun bindNotificationManager(impl: NotificationManagerImpl): NotificationManager

        @Binds
        @Singleton
        fun bindAppSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository

        @Binds
        @Singleton
        fun bindPermissionManager(impl: PermissionManagerImpl): PermissionManager
    }
}