package ru.mamykin.foboreader.read_book.presentation.view

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import ru.mamykin.foboreader.core.extension.allWordPositions

class ClickableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    private var onClick: ((String) -> Unit)? = null
    private var onLongClick: ((String) -> Unit)? = null

    @ColorInt
    private var userTextColor: Int = currentTextColor

    init {
        movementMethod = LongClickableMovementMethod()
        highlightColor = Color.TRANSPARENT
    }

    fun setOnParagraphClickListener(onClick: (paragraph: String) -> Unit) {
        this.onClick = onClick
    }

    fun setOnWordLongClickListener(onLongClick: (word: String) -> Unit) {
        this.onLongClick = onLongClick
    }

    fun setup(text: CharSequence) {
        setText(text)
        updateWordSpans()
    }

    private fun getSelectedWord(selectionStart: Int, selectionEnd: Int): String {
        return text.subSequence(selectionStart, selectionEnd)
            .trim(' ')
            .toString()
    }

    private fun updateWordSpans() {
        val spans = text as Spannable
        val spaceIndexes = text.trim().allWordPositions()
        var wordStart = 0
        var wordEnd: Int
        for (i in 0..spaceIndexes.size) {
            val clickableSpan = createLongClickableSpan()
            wordEnd = if (i < spaceIndexes.size) spaceIndexes[i] else spans.length
            spans.setSpan(clickableSpan, wordStart, wordEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            wordStart = wordEnd + 1
        }
    }

    private fun getSelectedParagraph(selectionStart: Int, selectionEnd: Int): String {
        val paragraphStart = findLeftLineBreak(text, selectionStart)
        val paragraphEnd = findRightLineBreak(text, selectionEnd)
        return text.toString().substring(paragraphStart, paragraphEnd)
    }

    private fun findLeftLineBreak(text: CharSequence, selStart: Int): Int {
        for (i in selStart downTo 0) {
            if (text[i] == '\n') return i + 1
        }
        return 0
    }

    private fun findRightLineBreak(text: CharSequence, selEnd: Int): Int {
        for (i in selEnd until text.length) {
            if (text[i] == '\n') return i + 1
        }
        return text.length - 1
    }

    private fun createLongClickableSpan() = object : LongClickableSpan(userTextColor) {

        override fun onClick(view: View, selStart: Int, selEnd: Int) {
            getSelectedParagraph(selStart, selEnd).takeIf { it.isNotEmpty() }
                ?.let { onClick?.invoke(it) }
        }

        override fun onLongClick(view: View, selStart: Int, selEnd: Int) {
            getSelectedWord(selStart, selEnd).takeIf { it.isNotEmpty() }
                ?.let { onLongClick?.invoke(it) }
        }
    }
}