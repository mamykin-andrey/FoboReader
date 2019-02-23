package ru.mamykin.foboreader.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import ru.mamykin.foboreader.data.database.converter.DateTimestampConverter
import ru.mamykin.foboreader.data.database.converter.SortOrderStringConverter
import ru.mamykin.foboreader.domain.entity.FictionBook

@Database(entities = [FictionBook::class], version = 1)
@TypeConverters(DateTimestampConverter::class, SortOrderStringConverter::class)
abstract class BooksDatabase : RoomDatabase() {

    abstract fun getBookDao(): BookDao
}