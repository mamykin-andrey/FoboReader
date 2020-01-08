package ru.mamykin.my_books.di

import android.content.Context
import androidx.room.Room
import ru.mamykin.my_books.data.database.BookInfoDao
import ru.mamykin.my_books.data.database.BookInfoDatabase

object DatabaseDependencies {

    fun dao(context: Context): BookInfoDao {
        return Room.databaseBuilder(context, BookInfoDatabase::class.java, "book_info")
                .build()
                .getBookInfoDao()
    }
}