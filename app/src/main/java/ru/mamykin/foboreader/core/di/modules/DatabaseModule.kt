package ru.mamykin.foboreader.core.di.modules

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.database.BooksDatabase
import javax.inject.Singleton

@Module
@Singleton
class DatabaseModule {

    @Provides
    @Singleton
    fun provideBooksDatabase(context: Context): BooksDatabase {
        return Room.databaseBuilder(context, BooksDatabase::class.java, "books").build()
    }

    @Provides
    @Singleton
    fun provideBookDao(booksDatabase: BooksDatabase): BookDao {
        return booksDatabase.getBookDao()
    }
}