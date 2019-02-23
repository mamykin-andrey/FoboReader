package ru.mamykin.foboreader.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.PreferencesManager
import javax.inject.Singleton

@Module
@Singleton
class PreferencesModule {

    @Provides
    @Singleton
    internal fun providePreferencesManager(context: Context): PreferencesManager {
        return PreferencesManager(context)
    }
}