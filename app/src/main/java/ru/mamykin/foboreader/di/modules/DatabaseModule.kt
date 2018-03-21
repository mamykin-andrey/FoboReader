package ru.mamykin.foboreader.di.modules

import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.database.BookDatabaseHelper

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
class DatabaseModule {
    @Provides
    @Singleton
    internal fun provideBooksDatabaseHelper(context: Context): BookDatabaseHelper {
        return BookDatabaseHelper(context)
    }

    @Provides
    @Singleton
    internal fun provideBookDao(dbHelper: BookDatabaseHelper): BookDao {
        return dbHelper.getBookDao()!!
    }
}