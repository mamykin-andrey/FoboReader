package ru.mamykin.foboreader.ui.global.widget

import android.os.Handler
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.view.MotionEvent
import android.widget.TextView

/**
 * Класс для [ru.mamykin.foboreader.ui.controls.SwipeableTextView], для определения
 * клика, лонг тапа, и горизонталных свайпов
 */
class SwipeableMovementMethod : LinkMovementMethod() {
    //private boolean longClick = false;
    private var longClickHandler: Handler? = null
    private var xStart: Double = 0.toDouble()
    private var tStart: Long = 0

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action

        //longClick = false;
        if (action == MotionEvent.ACTION_CANCEL && longClickHandler != null) {
            // Отмена
            longClickHandler!!.removeCallbacksAndMessages(null)
        } else if (action == MotionEvent.ACTION_DOWN) {
            // Нажатие
            xStart = event.x.toDouble()
            tStart = event.eventTime
            val link = getClickableSpan(event, widget, buffer)
            Selection.setSelection(buffer,
                    buffer.getSpanStart(link),
                    buffer.getSpanEnd(link))
            // Long click
            longClickHandler!!.postDelayed({
                //longClick = true;
                link.onLongClick(widget)
            }, 1000)
            return true
        } else if (action == MotionEvent.ACTION_UP) {
            // Отпускание
            val xDiff = event.x - xStart
            val tDiff = event.eventTime - tStart
            if (Math.abs(xDiff) > 100) {
                // Horizontal swipe
                cancelLongClick()
                val link = getClickableSpan(event, widget, buffer)
                if (xDiff > 0)
                    link.onSwipeRight(widget)
                else if (xDiff < 0)
                    link.onSwipeLeft(widget)
            } else if (tDiff < 1000) {
                // Click
                cancelLongClick()
                val link = getClickableSpan(event, widget, buffer)
                link.onClick(widget)
            }
            return true
        }
        return true
    }

    private fun cancelLongClick() {
        if (longClickHandler != null) {
            longClickHandler!!.removeCallbacksAndMessages(null)
        }
    }

    private fun getClickableSpan(event: MotionEvent, widget: TextView, buffer: Spannable): SwipeableSpan {
        var x = event.x.toInt()
        var y = event.y.toInt()
        x -= widget.totalPaddingLeft
        y -= widget.totalPaddingTop
        x += widget.scrollX
        y += widget.scrollY

        val layout = widget.layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        val spans = buffer.getSpans<SwipeableSpan>(off, off, SwipeableSpan::class.java)
        return spans[0]
    }

    companion object {
        private var instance: SwipeableMovementMethod? = null

        fun getInstance(): MovementMethod {
            if (instance == null) {
                instance = SwipeableMovementMethod()
                instance!!.longClickHandler = Handler()
            }

            return instance as SwipeableMovementMethod
        }
    }
}