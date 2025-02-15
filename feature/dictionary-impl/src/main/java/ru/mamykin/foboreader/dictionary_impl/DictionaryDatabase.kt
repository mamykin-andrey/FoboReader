package ru.mamykin.foboreader.dictionary_impl

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DictionaryWordDBModel::class], version = 2)
@TypeConverters(DateTimestampConverter::class)
abstract class DictionaryDatabase : RoomDatabase() {

    abstract fun getBookInfoDao(): WordDictionaryDao
}