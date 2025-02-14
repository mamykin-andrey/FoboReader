package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import javax.inject.Inject

internal class AddToDictionaryUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) {
    suspend fun execute(word: String, translation: String) {
        dictionaryRepository.addWord(word, translation)
    }
}