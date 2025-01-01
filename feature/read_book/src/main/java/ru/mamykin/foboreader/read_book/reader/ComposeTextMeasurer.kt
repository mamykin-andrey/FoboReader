package ru.mamykin.foboreader.read_book.reader

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp

internal class ComposeTextMeasurer(
    private val measurer: androidx.compose.ui.text.TextMeasurer,
) : TextMeasurer {

    override fun isTextFit(text: String, screenSize: Pair<Int, Int>, fontSize: Int): Boolean {
        val (height, width) = screenSize
        val result = measurer.measure(
            text = AnnotatedString(text),
            overflow = TextOverflow.Ellipsis,
            constraints = Constraints.fixed(width, height),
            style = TextStyle(fontSize = fontSize.sp),
        )
        val lastCharacterIndex = result.getLineEnd(result.lineCount - 1, true)
        return lastCharacterIndex >= text.length
    }
}