package ru.mamykin.foreignbooksreader.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
public class AppModule {
    private Context context;

    public AppModule(@NonNull Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }
}