package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.core.extension.trimSpecialCharacters
import ru.mamykin.foboreader.read_book.data.TranslationRepository
import ru.mamykin.foboreader.read_book.domain.model.TextTranslation
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