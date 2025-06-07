package ru.mamykin.foboreader.dictionary_impl

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.dictionary_api.DictionaryWord
import ru.mamykin.foboreader.dictionary_api.GetWordsToLearnUseCase
import javax.inject.Inject

class GetWordsToLearnUseCaseImpl @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : GetWordsToLearnUseCase {
    override suspend fun execute(): List<DictionaryWord> {
        return dictionaryRepository.getAllWords()
    }
}