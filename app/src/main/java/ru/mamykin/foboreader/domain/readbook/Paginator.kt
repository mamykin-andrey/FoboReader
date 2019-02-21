package ru.mamykin.foboreader.domain.readbook

import android.text.Layout
import android.text.StaticLayout
import ru.mamykin.foboreader.entity.ViewParams

/**
 * Class, which implements pagination in TextView
 */
class Paginator(content: CharSequence, viewParams: ViewParams) {

    private val layout = StaticLayout(
            content,
            viewParams.paint,
            viewParams.width,
            Layout.Alignment.ALIGN_NORMAL,
            viewParams.spacingMult,
            viewParams.spacingAdd,
            viewParams.includePad
    )

    private val pages: List<CharSequence> = parsePages(layout, viewParams.height)

    var currentIndex = 0

    val pagesCount: Int = pages.size

    fun getReadPercent(): Float = when (pagesCount) {
        0 -> 0f
        else -> (currentIndex + 1) / pagesCount.toFloat() * 100
    }

    fun getCurrentPage(): CharSequence = pages[currentIndex]

    private fun parsePages(layout: StaticLayout, height: Int): List<CharSequence> {
        val lines = layout.lineCount
        val text = layout.text
        var startOffset = 0
        var bottomLineHeight = height

        val parsedPages = mutableListOf<CharSequence>()
        for (i in 0 until lines) {
            if (bottomLineHeight < layout.getLineBottom(i)) {
                val page = text.subSequence(startOffset, layout.getLineStart(i))
                parsedPages.add(page)
                startOffset = layout.getLineStart(i)
                bottomLineHeight = layout.getLineTop(i) + height
            }
        }
        val lastPage = text.subSequence(startOffset, layout.getLineEnd(lines - 1))
        return parsedPages.plus(lastPage)
    }
}