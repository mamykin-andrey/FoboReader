package ru.mamykin.foboreader.read_book.translation

import ru.mamykin.foboreader.core.extension.trimSpecialCharacters
import javax.inject.Inject

internal class GetWordTranslation @Inject constructor(
    private val translationRepository: TranslationRepository
) {
    suspend fun execute(text: String): Result<TextTranslation> {
        val word = text.trimSpecialCharacters()
        return runCatching {
            translationRepository.getTranslation(word)
        }
    }
}