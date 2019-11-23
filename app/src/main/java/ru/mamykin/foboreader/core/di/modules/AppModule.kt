package ru.mamykin.foboreader.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@Singleton
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context
}