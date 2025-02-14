package ru.mamykin.foboreader.dictionary_impl

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.DictionaryWord
import javax.inject.Inject

class RoomDictionaryRepository @Inject constructor() : DictionaryRepository {

    override suspend fun getAllWords(): List<DictionaryWord> {
        return emptyList()
    }

    override suspend fun addWord(word: String, translation: String) {
    }
}