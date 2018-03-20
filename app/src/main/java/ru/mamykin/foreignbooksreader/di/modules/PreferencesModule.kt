package ru.mamykin.foreignbooksreader.di.modules

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import ru.mamykin.foreignbooksreader.data.storage.PreferencesManager

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
class PreferencesModule {
    @Provides
    @Singleton
    internal fun providePreferencesManager(): PreferencesManager {
        return PreferencesManager()
    }
}