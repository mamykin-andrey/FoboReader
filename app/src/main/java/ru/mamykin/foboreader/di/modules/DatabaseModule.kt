package ru.mamykin.foboreader.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.database.BookDatabaseHelper
import javax.inject.Singleton

@Module
@Singleton
class DatabaseModule {

    @Provides
    @Singleton
    fun provideBooksDatabaseHelper(context: Context): BookDatabaseHelper {
        return BookDatabaseHelper(context)
    }

    @Provides
    @Singleton
    fun provideBookDao(dbHelper: BookDatabaseHelper): BookDao {
        return dbHelper.getBookDao()
    }
}