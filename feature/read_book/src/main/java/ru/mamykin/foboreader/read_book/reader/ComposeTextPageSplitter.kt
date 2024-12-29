package ru.mamykin.foboreader.read_book.reader

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp

internal class ComposeTextPageSplitter(
    private val measurer: androidx.compose.ui.text.TextMeasurer,
) : TextSplitter {

    override fun splitText(text: String, screenSize: Pair<Int, Int>, fontSize: Int): List<String> {
        val (height, width) = screenSize
        val pages = mutableListOf<String>()
        var remainingText = text
        while (remainingText.isNotEmpty()) {
            val result = measurer.measure(
                text = AnnotatedString(remainingText),
                overflow = TextOverflow.Ellipsis,
                constraints = Constraints.fixed(width, height),
                style = TextStyle(fontSize = fontSize.sp),
            )
            val lastCharacterIndex = result.getLineEnd(result.lineCount - 1, true)
            if (lastCharacterIndex >= remainingText.lastIndex) {
                pages.add(remainingText)
                remainingText = ""
            } else if (lastCharacterIndex > 0) {
                var pageEnd = lastCharacterIndex
                val lastCharacter = remainingText.getOrNull(pageEnd - 1)
                val nextPageCharacter = remainingText.getOrNull(pageEnd)
                if (lastCharacter?.isLetter() == true && nextPageCharacter?.isLetter() == true) {
                    while (pageEnd > 0 && !remainingText[pageEnd - 1].isWhitespace()) {
                        pageEnd--
                    }
                    if (pageEnd == 0) {
                        pageEnd = lastCharacterIndex
                    }
                }
                pages.add(remainingText.substring(0, pageEnd).trim())
                remainingText = remainingText.substring(pageEnd).trim()
            } else {
                break
            }
        }

        return pages
    }
}