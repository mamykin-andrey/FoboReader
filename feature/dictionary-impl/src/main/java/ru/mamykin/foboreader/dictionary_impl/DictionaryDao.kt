package ru.mamykin.foboreader.dictionary_impl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DictionaryDao {

    @Insert
    suspend fun insert(book: DictionaryWordDBModel)

    @Query("SELECT * FROM dictionaryworddbmodel")
    suspend fun getWords(): List<DictionaryWordDBModel>
}