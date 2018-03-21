package ru.mamykin.foboreader.di.modules

import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return context
    }
}