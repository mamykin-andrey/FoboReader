package ru.mamykin.foboreader.read_book.reader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class GetBookUseCase @Inject constructor(
    private val bookContentRepository: BookContentRepository,
    private val bookInfoRepository: BookInfoRepository,
    private val appSettingsRepository: AppSettingsRepository,
) {
    suspend fun execute(
        bookId: Long,
        textMeasurer: TextMeasurer,
        screenSize: Pair<Int, Int>
    ): Book = withContext(Dispatchers.Default) {
        val fontSize = appSettingsRepository.getReadTextSize()
        val info = bookInfoRepository.getBookInfo(bookId)
        val content = bookContentRepository.getBookContent(info.filePath)
        val pages = splitText(
            sentences = content.sentences,
            translations = content.translations,
            screenSize = screenSize,
            fontSize = fontSize,
            textMeasurer = textMeasurer,
        )
        return@withContext Book(
            info = info,
            pages = pages,
            userSettings = Book.UserSettings(
                fontSize = fontSize,
                translationColorCode = appSettingsRepository.getTranslationColor(),
                backgroundColorCode = appSettingsRepository.getBackgroundColor(),
            ),
        )
    }

    private fun splitText(
        sentences: List<String>,
        translations: List<String>,
        screenSize: Pair<Int, Int>,
        fontSize: Int,
        textMeasurer: TextMeasurer
    ): MutableList<Book.Page> {
        val result = mutableListOf<Book.Page>()
        var remainingSentences = sentences
        var remainingTranslations = translations
        while (remainingSentences.isNotEmpty()) {
            val resultSentences = mutableListOf<String>()
            val resultTranslations = mutableListOf<String>()
            var i = 1
            while (i <= remainingSentences.size) {
                val newText = remainingSentences.subList(0, i).joinToString("\n")
                if (textMeasurer.isTextFit(newText, screenSize, fontSize)) {
                    resultSentences.add(remainingSentences[i - 1])
                    resultTranslations.add(remainingTranslations[i - 1])
                } else {
                    break
                }
                i++
            }
            result.add(Book.Page(sentences = resultSentences, translations = resultTranslations))
            remainingSentences = remainingSentences.drop(i - 1)
            remainingTranslations = remainingTranslations.drop(i - 1)
        }
        return result
    }
}