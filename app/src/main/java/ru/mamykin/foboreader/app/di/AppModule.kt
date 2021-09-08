package ru.mamykin.foboreader.app.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.app.navigation.ScreenProviderImpl
import ru.mamykin.foboreader.app.presentation.tabs.TabsViewModel
import ru.mamykin.foboreader.core.data.OkHttpFactory
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import javax.inject.Singleton

@Module(includes = [AppModule.BindsModule::class])
class AppModule {

    @Provides
    fun provideTabsViewModel() = TabsViewModel()

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
    }
}