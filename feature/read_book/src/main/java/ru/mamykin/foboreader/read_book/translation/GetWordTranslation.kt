package ru.mamykin.foboreader.read_book.translation

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

    private fun String.trimSpecialCharacters(): String {
        // TODO: do not trim the whole text, trim only left and right sides
        return this.replace("[^a-zA-ZА-ЯЁа-яё0-9]".toRegex(), "")
    }
}