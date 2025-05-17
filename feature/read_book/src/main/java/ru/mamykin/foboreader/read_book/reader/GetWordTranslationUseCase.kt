package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.dictionary_api.DictionaryRepository
import ru.mamykin.foboreader.read_book.translation.TranslationRepository
import ru.mamykin.foboreader.read_book.translation.WordTranslation
import javax.inject.Inject

internal class GetWordTranslationUseCase @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val translationRepository: TranslationRepository,
) {
    suspend fun execute(dictionary: Map<String, String>, rawWord: String): WordTranslation? {
        val word = rawWord.trimSpecialCharacters()
        val translation = dictionary[word]
            ?: translationRepository.getTranslation(word).getOrNull()?.translation
        return translation?.let {
            WordTranslation(
                word,
                translation,
                dictionaryRepository.getWord(word)?.id,
            )
        }
    }

    private fun String.trimSpecialCharacters(): String {
        // TODO: do not trim the whole text, trim only left and right sides
        return this.replace("[^a-zA-ZА-ЯЁа-яё0-9]".toRegex(), "")
    }
}