package ru.mamykin.foboreader.read_book.reader

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import javax.inject.Inject

// TODO: use real text size
internal class GetBookUseCase @Inject constructor(
    private val bookContentRepository: BookContentRepository,
    private val bookInfoRepository: BookInfoRepository
) {
    suspend fun execute(
        bookId: Long,
        measurer: TextMeasurer,
        screenSize: Pair<Int, Int>
    ): Book = withContext(Dispatchers.Default) {
        val info = bookInfoRepository.getBookInfo(bookId)
        val content = bookContentRepository.getBookContent(info.filePath)
        val pages = splitTextToPages(content.text, measurer, screenSize)
        return@withContext Book(
            info = info,
            content = content,
            pages = pages,
        )
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