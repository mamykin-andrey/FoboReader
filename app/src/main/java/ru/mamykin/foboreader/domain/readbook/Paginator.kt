package ru.mamykin.foboreader.domain.readbook

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

/**
 * Class, that implements pagination in TextView
 */
class Paginator(content: CharSequence,
                width: Int,
                height: Int,
                paint: TextPaint,
                spacingMult: Float,
                spacingAdd: Float,
                includePad: Boolean
) {
    private val pages = mutableListOf<CharSequence>()

    var currentIndex = 0

    val pagesCount: Int = pages.size

    val currentPage: CharSequence = pages[currentIndex]

    val readPercent: Float = when (pagesCount) {
        0 -> 0f
        else -> (currentIndex + 1) / pagesCount.toFloat() * 100
    }

    init {
        val layout = StaticLayout(
                content,
                paint, width,
                Layout.Alignment.ALIGN_NORMAL,
                spacingMult,
                spacingAdd,
                includePad
        )
        fillPages(layout, height)
    }

    private fun fillPages(layout: StaticLayout, height: Int) {
        val lines = layout.lineCount
        val text = layout.text
        var startOffset = 0
        var bottomLineHeight = height

        for (i in 0 until lines) {
            if (bottomLineHeight < layout.getLineBottom(i)) {
                pages.add(text.subSequence(startOffset, layout.getLineStart(i)))
                startOffset = layout.getLineStart(i)
                bottomLineHeight = layout.getLineTop(i) + height
            }
        }
        pages.add(text.subSequence(startOffset, layout.getLineEnd(lines - 1)))
    }
}