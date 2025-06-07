package ru.mamykin.foboreader.dictionary_impl

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.DictionaryWord
import java.util.Date
import javax.inject.Inject
import ru.mamykin.foboreader.core.data.storage.PreferencesManager

class RoomDictionaryRepository @Inject constructor(
    private val dao: WordDictionaryDao,
    private val prefManager: PreferencesManager,
) : DictionaryRepository {

    companion object {
        private const val KEY_COMPLETION_DATES = "completion_dates"
    }

    override suspend fun getAllWords(): List<DictionaryWord> {
        return dao.getAll().map { it.toDomainModel() }
    }

    override suspend fun addWord(word: String, translation: String): Long {
        return dao.insert(DictionaryWordDBModel(0, Date(), word, translation))
    }

    override suspend fun removeWord(wordId: Long) {
        dao.remove(wordId)
    }

    override suspend fun getWord(word: String): DictionaryWord? {
        return dao.getByWord(word)?.toDomainModel()
    }

    override suspend fun getCompletionDates(): List<String> {
        val completionDatesStr = prefManager.getString(KEY_COMPLETION_DATES, "")
        return completionDatesStr.split(",").filter { it.isNotEmpty() }
    }

    override suspend fun addCompletionDate(date: String) {
        val completionDates = getCompletionDates() + date
        val completionDatesStr = completionDates.joinToString(",")
        prefManager.putString(KEY_COMPLETION_DATES, completionDatesStr)
    }
}