package ru.mamykin.foboreader.my_books.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mamykin.foboreader.my_books.data.database.converter.DateTimestampConverter
import ru.mamykin.foboreader.my_books.data.database.converter.StringListConverter
import ru.mamykin.foboreader.my_books.data.model.BookInfoModel

@Database(entities = [BookInfoModel::class], version = 1)
@TypeConverters(DateTimestampConverter::class, StringListConverter::class)
abstract class BookInfoDatabase : RoomDatabase() {

    abstract fun getBookInfoDao(): BookInfoDao
}