package ru.mamykin.foboreader.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mamykin.foboreader.data.database.converter.DateTimestampConverter
import ru.mamykin.foboreader.data.database.converter.SortOrderStringConverter
import ru.mamykin.foboreader.domain.entity.FictionBook

@Database(entities = [FictionBook::class], version = 1)
@TypeConverters(DateTimestampConverter::class, SortOrderStringConverter::class)
abstract class BooksDatabase : RoomDatabase() {

    abstract fun getBookDao(): BookDao
}