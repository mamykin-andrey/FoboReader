package ru.mamykin.foboreader.read_book.reader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mamykin.foboreader.common_book_info.domain.GetBookInfoUseCase
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.read_book.translation.TextTranslation
import javax.inject.Inject

internal class GetBookUseCase @Inject constructor(
    private val bookContentRepository: BookContentRepository,
    private val getBookInfoUseCase: GetBookInfoUseCase,
    private val appSettingsRepository: AppSettingsRepository,
) {
    suspend fun execute(
        bookId: Long,
        textMeasurer: TextMeasurer,
        screenSize: Pair<Int, Int>
    ): Result<Book> = runCatching {
        withContext(Dispatchers.Default) {
            val fontSize = appSettingsRepository.getReadTextSize()
            val info = getBookInfoUseCase.execute(bookId)
            val content = bookContentRepository.getBookContent(info.filePath)
            val pages = splitText(
                sentences = content.sentences,
                screenSize = screenSize,
                fontSize = fontSize,
                textMeasurer = textMeasurer,
            )
            Book(
                info = info,
                pages = pages,
                dictionary = content.dictionary,
                userSettings = Book.UserSettings(
                    fontSize = fontSize,
                    translationColorCode = appSettingsRepository.getTranslationColor(),
                    backgroundColorCode = appSettingsRepository.getBackgroundColor(),
                ),
            )
        }
    }

    /**
     * Splits text into pages. The book format was recently adjusted, maybe I should reconsider the final structure
     * since now original paragraphs and their translations are stored in the same struct.
     */
    private fun splitText(
        sentences: List<TextTranslation>,
        screenSize: Pair<Int, Int>,
        fontSize: Int,
        textMeasurer: TextMeasurer
    ): List<Book.Page> {
        val result = mutableListOf<Book.Page>()
        var remainingSentences = sentences
        while (remainingSentences.isNotEmpty()) {
            val resultSentences = mutableListOf<String>()
            val resultTranslations = mutableListOf<String>()
            var i = 1
            while (i <= remainingSentences.size) {
                val newText = remainingSentences.subList(0, i).joinToString("\n") { it.source }
                if (textMeasurer.isTextFit(newText, screenSize, fontSize)) {
                    val sentence = remainingSentences[i - 1]
                    resultSentences.add(sentence.source)
                    resultTranslations.add(sentence.translation)
                } else {
                    break
                }
                i++
            }
            result.add(Book.Page(sentences = resultSentences, translations = resultTranslations))
            remainingSentences = remainingSentences.drop(i - 1)
        }
        return result
    }
}