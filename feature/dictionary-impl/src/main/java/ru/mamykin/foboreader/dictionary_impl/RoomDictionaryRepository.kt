package ru.mamykin.foboreader.dictionary_impl

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.DictionaryWord
import java.util.Date
import javax.inject.Inject

class RoomDictionaryRepository @Inject constructor(
    private val dao: DictionaryDao,
) : DictionaryRepository {

    override suspend fun getAllWords(): List<DictionaryWord> {
        return dao.getWords().map { it.toDomainModel() }
    }

    override suspend fun addWord(word: String, translation: String) {
        dao.insert(DictionaryWordDBModel(0, Date(), word, translation))
    }
}