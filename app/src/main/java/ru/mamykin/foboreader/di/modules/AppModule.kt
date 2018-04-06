package ru.mamykin.foboreader.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.presentation.global.AppSchedulers
import ru.mamykin.foboreader.presentation.global.Schedulers
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