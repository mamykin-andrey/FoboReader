package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import javax.inject.Inject

internal class GetDictionaryWordsUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository
) {
    suspend fun execute(): Set<String> {
        return try {
            dictionaryRepository.getAllWords().map { it.word.lowercase() }.toSet()
        } catch (e: Exception) {
            emptySet()
        }
    }
} 