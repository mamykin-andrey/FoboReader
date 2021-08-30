package ru.mamykin.foboreader.common_book_info.di

import android.content.Context
import androidx.room.Room
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDatabase

object BookInfoDaoFactory {

    fun create(context: Context): BookInfoDao {
        return Room.databaseBuilder(context, BookInfoDatabase::class.java, "book_info")
            .build()
            .getBookInfoDao()
    }
}