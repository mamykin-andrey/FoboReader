package ru.mamykin.foboreader.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.core.platform.AppSchedulers
import ru.mamykin.foboreader.core.platform.Schedulers
import javax.inject.Singleton

@Module
@Singleton
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideSchedulers(appSchedulers: AppSchedulers): Schedulers = appSchedulers
}