package ru.mamykin.foreignbooksreader.domain.readbook

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

import java.util.ArrayList

class Paginator(content: CharSequence,
                width: Int,
                height: Int,
                paint: TextPaint,
                spacingMult: Float,
                spacingAdd: Float,
                includePad: Boolean
) {

    private val pagesList = ArrayList<CharSequence>()
    var currentIndex = 0

    val pagesCount: Int
        get() = pagesList.size

    val currentPage: CharSequence?
        get() = getPage(currentIndex)

    val readPercent: Float
        get() = (currentIndex + 1) / pagesCount.toFloat() * 100

    val readPages: String
        get() = (currentIndex + 1).toString() + "/" + pagesCount

    init {
        val layout = StaticLayout(content, paint, width,
                Layout.Alignment.ALIGN_NORMAL, spacingMult, spacingAdd, includePad)

        val lines = layout.lineCount
        val text = layout.text
        var startOffset = 0
        var lHeight = height

        for (i in 0 until lines) {
            if (lHeight < layout.getLineBottom(i)) {
                addPage(text.subSequence(startOffset, layout.getLineStart(i)))
                startOffset = layout.getLineStart(i)
                lHeight = layout.getLineTop(i) + height
            }
            if (i == lines - 1) {
                addPage(text.subSequence(startOffset, layout.getLineEnd(i)))
                break
            }
        }
    }

    private fun addPage(text: CharSequence) {
        pagesList.add(text)
    }

    fun getPage(index: Int): CharSequence? {
        return if (index >= 0 && index < pagesList.size) pagesList[index] else null
    }
}