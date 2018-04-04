package ru.mamykin.foboreader.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ru.mamykin.foboreader.entity.FictionBook

@Database(entities = [FictionBook::class], version = 1)
abstract class BooksDatabase : RoomDatabase() {

    abstract fun getBookDao(): BookDao
}