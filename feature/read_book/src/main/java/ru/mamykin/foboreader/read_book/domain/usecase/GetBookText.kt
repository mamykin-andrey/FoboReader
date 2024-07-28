package ru.mamykin.foboreader.read_book.domain.usecase

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mamykin.foboreader.read_book.data.BookContentRepository
import javax.inject.Inject

// TODO: use real text size
internal class GetBookText @Inject constructor(
    private val repository: BookContentRepository,
) {
    suspend fun execute(
        filePath: String,
        measurer: TextMeasurer,
        screenSize: Pair<Int, Int>
    ): List<String> = withContext(
        Dispatchers.Default
    ) {
        val fullText = repository.getBookContent(filePath).text
        return@withContext splitTextToPages(fullText, measurer, screenSize)
    }

    private fun splitTextToPages(
        text: String,
        measurer: TextMeasurer,
        screenSize: Pair<Int, Int>,
    ): List<String> {
        val words = text.split("(?<=\\s)|(?=\\s)".toRegex())
        val currentPage = StringBuilder()
        val pages = mutableListOf<String>()
        for (word in words) {
            if (!doesTheTextFit(
                    text = currentPage.toString() + word,
                    measurer = measurer,
                    screenSize = screenSize,
                )
            ) {
                pages.add(currentPage.toString())
                currentPage.clear()
            }
            currentPage.append(word)
        }
        currentPage.takeIf(StringBuilder::isNotEmpty)?.toString()?.also(pages::add)
        return pages
    }

    private fun doesTheTextFit(
        text: String,
        measurer: TextMeasurer,
        screenSize: Pair<Int, Int>,
    ): Boolean {
        val (availableHeight, availableWidth) = screenSize
        val textSize = measureTextSize(
            text = text,
            measurer = measurer,
            availableWidth = availableWidth,
        )
        return textSize.size.height <= availableHeight
    }

    private fun measureTextSize(
        text: String,
        measurer: TextMeasurer,
        availableWidth: Int,
    ): TextLayoutResult {
        return measurer.measure(
            text = AnnotatedString(text),
            style = TextStyle(fontSize = 18.sp),
            constraints = Constraints(maxWidth = availableWidth),
        )
    }
}