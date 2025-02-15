package ru.mamykin.foboreader.dictionary_api

interface DictionaryRepository {
    suspend fun getAllWords(): List<DictionaryWord>
    suspend fun addWord(word: String, translation: String)
    suspend fun removeWord(wordId: Long)
    suspend fun getWord(word: String): DictionaryWord?
}