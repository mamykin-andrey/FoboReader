package ru.mamykin.foboreader.read_book.presentation.view

import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance
import android.view.View

abstract class LongClickableSpan(
    private val textColor: Int
) : CharacterStyle(),
    UpdateAppearance {

    abstract fun onClick(view: View, selStart: Int, selEnd: Int)

    abstract fun onLongClick(view: View, selStart: Int, selEnd: Int)

    override fun updateDrawState(paint: TextPaint) {
        paint.isUnderlineText = false
        paint.color = textColor
    }
}