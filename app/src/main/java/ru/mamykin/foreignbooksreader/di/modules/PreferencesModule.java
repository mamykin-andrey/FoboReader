package ru.mamykin.foreignbooksreader.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
public class PreferencesModule {
    @Provides
    @Singleton
    PreferencesManager providePreferencesManager() {
        return new PreferencesManager();
    }
}