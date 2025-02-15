package ru.mamykin.foboreader.dictionary_impl

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.DictionaryWord
import java.util.Date
import javax.inject.Inject

class RoomDictionaryRepository @Inject constructor(
    private val dao: WordDictionaryDao,
) : DictionaryRepository {

    override suspend fun getAllWords(): List<DictionaryWord> {
        return dao.getAll().map { it.toDomainModel() }
    }

    override suspend fun addWord(word: String, translation: String) {
        dao.insert(DictionaryWordDBModel(0, Date(), word, translation))
    }

    override suspend fun removeWord(wordId: Long) {
        dao.remove(wordId)
    }

    override suspend fun getWord(word: String): DictionaryWord? {
        return dao.getByWord(word)?.toDomainModel()
    }
}