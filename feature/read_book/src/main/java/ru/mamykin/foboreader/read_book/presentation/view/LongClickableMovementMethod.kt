package ru.mamykin.foboreader.read_book.presentation.view

import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView
import kotlin.math.abs

/**
 * An extended version of LinkMovement method, which also support long clicks and swipes
 */
class LongClickableMovementMethod : LinkMovementMethod() {

    companion object {

        const val MIN_LONG_CLICK_TIME = 100
        const val MIN_SWIPE_DISTANCE = 100
        const val DEFAULT_LONG_CLICK_DELAY = 1000L
    }

    private val longClickHandler = Handler(Looper.getMainLooper())

    private var startX: Double = 0.0
    private var startY: Double = 0.0
    private var startTime: Long = 0

    private var clickedSpanStart: Int = 0
    private var clickedSpanEnd: Int = 0

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        // TODO: check for swipes with helper
        when (event.action) {
            MotionEvent.ACTION_CANCEL -> handleCancelAction()
            MotionEvent.ACTION_DOWN -> handleDownAction(event, buffer, widget)
            MotionEvent.ACTION_UP -> handleUpAction(event, buffer, widget)
            MotionEvent.ACTION_MOVE -> handleMoveAction(event)
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    private fun handleMoveAction(event: MotionEvent) {
        if (isSwipe(event.x, event.y)) {
            longClickHandler.removeCallbacksAndMessages(null)
        }
    }

    private fun handleCancelAction() {
        longClickHandler.removeCallbacksAndMessages(null)
    }

    private fun handleDownAction(event: MotionEvent, buffer: Spannable, widget: TextView) {
        startX = event.x.toDouble()
        startY = event.y.toDouble()
        startTime = event.eventTime

        val link = getClickableSpan(event, widget, buffer) ?: return
        clickedSpanStart = buffer.getSpanStart(link)
        clickedSpanEnd = buffer.getSpanEnd(link)
//        Selection.setSelection(buffer, buffer.getSpanStart(link), buffer.getSpanEnd(link))

        longClickHandler.postDelayed({ link.onLongClick(widget, clickedSpanStart, clickedSpanEnd) }, DEFAULT_LONG_CLICK_DELAY)
    }

    private fun handleUpAction(event: MotionEvent, buffer: Spannable, widget: TextView) {
        val timeDiff = event.eventTime - startTime
        if (timeDiff < MIN_LONG_CLICK_TIME) {
            longClickHandler.removeCallbacksAndMessages(null)
            val link = getClickableSpan(event, widget, buffer) ?: return
            if (!isSwipe(event.x, event.y)) {
                link.onClick(widget, clickedSpanStart, clickedSpanEnd)
            }
        }
    }

    private fun isSwipe(x: Float, y: Float): Boolean {
        val xDiff = abs(x - startX)
        val yDiff = abs(y - startY)
        return xDiff > MIN_SWIPE_DISTANCE || yDiff > MIN_SWIPE_DISTANCE
    }

    private fun getClickableSpan(
        event: MotionEvent,
        widget: TextView,
        buffer: Spannable
    ): LongClickableSpan? {
        val clickX = event.x - widget.totalPaddingLeft + widget.scrollX
        val clickY = event.y.toInt() - widget.totalPaddingTop + widget.scrollY

        val line = widget.layout.getLineForVertical(clickY)
        val offset = widget.layout.getOffsetForHorizontal(line, clickX)

        return buffer.getSpans(offset, offset, LongClickableSpan::class.java).firstOrNull()
    }
}