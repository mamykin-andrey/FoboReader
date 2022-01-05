package ru.mamykin.foboreader.app.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.*
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.app.navigation.ScreenProviderImpl
import ru.mamykin.foboreader.app.navigation.TabFragmentProviderImpl
import ru.mamykin.foboreader.app.platform.ResourceManagerImpl
import ru.mamykin.foboreader.core.data.OkHttpFactory
import ru.mamykin.foboreader.core.di.api.*
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.navigation.TabFragmentProvider
import ru.mamykin.foboreader.core.platform.ResourceManager
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
internal interface AppComponent : NavigationApi, NetworkApi, CommonApi, SettingsApi, MainApi {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance cicerone: Cicerone<Router>
        ): AppComponent
    }
}

@Module(includes = [AppModule.BindsModule::class])
internal class AppModule {

    @Provides
    @Singleton
    @CommonClient
    fun provideCommonClient(): OkHttpClient = OkHttpFactory.create(true)

    @Provides
    @Singleton
    fun provideRouter(cicerone: Cicerone<Router>): Router = cicerone.router

    @Module
    interface BindsModule {

        @Binds
        @Singleton
        fun bindScreenProvider(impl: ScreenProviderImpl): ScreenProvider

        @Binds
        @Singleton
        fun bindFragmentProvider(impl: TabFragmentProviderImpl): TabFragmentProvider

        @Binds
        @Singleton
        fun bindResourceManager(impl: ResourceManagerImpl): ResourceManager
    }
}