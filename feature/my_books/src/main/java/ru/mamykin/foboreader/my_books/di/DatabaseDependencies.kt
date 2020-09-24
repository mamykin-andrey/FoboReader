package ru.mamykin.foboreader.my_books.di

import android.content.Context
import androidx.room.Room
import ru.mamykin.foboreader.core.data.database.BookInfoDao
import ru.mamykin.foboreader.core.data.database.BookInfoDatabase

object DatabaseDependencies {

    fun dao(context: Context): BookInfoDao {
        return Room.databaseBuilder(context, BookInfoDatabase::class.java, "book_info")
            .build()
            .getBookInfoDao()
    }
}