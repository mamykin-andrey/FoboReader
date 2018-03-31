package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.storage.PreferencesManager
import javax.inject.Singleton

@Module
@Singleton
class PreferencesModule {

    @Provides
    @Singleton
    internal fun providePreferencesManager(): PreferencesManager {
        return PreferencesManager()
    }
}