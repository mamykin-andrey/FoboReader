package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import javax.inject.Inject

internal class RemoveFromDictionaryUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) {
    suspend fun execute(wordDictionaryId: Long) {
        dictionaryRepository.removeWord(wordDictionaryId)
    }
}