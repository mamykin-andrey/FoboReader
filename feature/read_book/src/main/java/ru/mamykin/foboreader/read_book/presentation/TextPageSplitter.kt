package ru.mamykin.foboreader.read_book.presentation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints

// TODO: refactor and make it a use case
class TextPageSplitter {

    fun splitTextToPages(
        text: String,
        measurer: TextMeasurer,
        availableHeight: Int,
        availableWidth: Int,
        bookTextStyle: TextStyle
    ): List<String> {
        val words = text.split("(?<=\\s)|(?=\\s)".toRegex())
        val currentPage = StringBuilder()
        val pages = mutableListOf<String>()
        for (word in words) {
            if (!doesTheTextFit(
                    text = currentPage.toString() + word,
                    measurer = measurer,
                    availableHeight = availableHeight,
                    availableWidth = availableWidth,
                    bookTextStyle = bookTextStyle,
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
        availableHeight: Int,
        availableWidth: Int,
        bookTextStyle: TextStyle,
    ): Boolean {
        val textSize = measureTextSize(
            text = text,
            measurer = measurer,
            availableWidth = availableWidth,
            bookTextStyle = bookTextStyle,
        )
        return textSize.size.height <= availableHeight
    }

    private fun measureTextSize(
        text: String,
        measurer: TextMeasurer,
        availableWidth: Int,
        bookTextStyle: TextStyle,
    ): TextLayoutResult {
        return measurer.measure(
            text = AnnotatedString(text),
            style = bookTextStyle,
            constraints = Constraints(maxWidth = availableWidth.toInt()),
        )
    }
}