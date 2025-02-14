package ru.mamykin.foboreader.dictionary_impl

import android.content.Context
import androidx.room.Room

object DictionaryDaoFactory {

    fun create(context: Context): DictionaryDao {
        return Room.databaseBuilder(context, DictionaryDatabase::class.java, "dictionary")
            .build()
            .getBookInfoDao()
    }
}