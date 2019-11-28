package ru.mamykin.core.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.data.database.BooksDatabase
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