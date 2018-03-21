package ru.mamykin.foboreader.ui.global.widget

import android.text.style.ClickableSpan
import android.view.View

/**
 * Класс для расширения Span лонг тапом и горизонтальными свайпами
 */
abstract class SwipeableSpan : ClickableSpan() {
    abstract fun onLongClick(view: View)

    abstract fun onSwipeLeft(view: View)

    abstract fun onSwipeRight(view: View)
}