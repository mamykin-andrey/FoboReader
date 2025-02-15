package ru.mamykin.foboreader.dictionary_impl

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDictionaryDao {

    @Insert
    suspend fun insert(book: DictionaryWordDBModel): Long

    @Query("DELETE FROM dictionaryworddbmodel WHERE id = :wordId")
    suspend fun remove(wordId: Long)

    @Query("SELECT * FROM dictionaryworddbmodel")
    suspend fun getAll(): List<DictionaryWordDBModel>

    @Query("SELECT * FROM dictionaryworddbmodel WHERE word = :word")
    suspend fun getByWord(word: String): DictionaryWordDBModel?
}