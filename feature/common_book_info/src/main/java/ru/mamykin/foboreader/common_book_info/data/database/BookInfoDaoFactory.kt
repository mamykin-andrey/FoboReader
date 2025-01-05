package ru.mamykin.foboreader.common_book_info.data.database

import android.content.Context
import androidx.room.Room

object BookInfoDaoFactory {

    fun create(context: Context): DownloadedBooksDao {
        return Room.databaseBuilder(context, BookInfoDatabase::class.java, "book_info")
            .build()
            .getBookInfoDao()
    }
}