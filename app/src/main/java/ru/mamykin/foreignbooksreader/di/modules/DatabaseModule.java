package ru.mamykin.foreignbooksreader.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.mamykin.foreignbooksreader.database.BookDao;
import ru.mamykin.foreignbooksreader.database.BookDatabaseHelper;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
public class DatabaseModule {
    @Provides
    @Singleton
    BookDatabaseHelper provideBooksDatabaseHelper(Context context) {
        return new BookDatabaseHelper(context);
    }

    @Provides
    @Singleton
    BookDao provideBookDao(BookDatabaseHelper dbHelper) {
        return dbHelper.getBookDao();
    }
}