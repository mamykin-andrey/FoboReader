package ru.mamykin.foboreader.dictionary_api

interface DictionaryRepository {
    suspend fun getAllWords(): List<DictionaryWord>
    suspend fun addWord(word: String, translation: String)
}