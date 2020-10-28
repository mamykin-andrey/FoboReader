package ru.mamykin.widget.paginatedtextview.view

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatTextView
import ru.mamykin.widget.paginatedtextview.extension.allWordPositions
import ru.mamykin.widget.paginatedtextview.pagination.PaginationController
import ru.mamykin.widget.paginatedtextview.pagination.ReadState

/**
 * An extended TextView, which support pagination, clicks by paragraphs and long clicks by words
 */
class PaginatedTextView @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    private var swipeListener: OnSwipeListener? = null
    private var actionListener: OnActionListener? = null
    private lateinit var controller: PaginationController
    private var isMeasured = false

    init {
        initPaginatedTextView()
    }

    override fun scrollTo(x: Int, y: Int) {}

    fun setup(text: CharSequence, currentPage: Int = 0) {
        if (isMeasured) {
            loadCurrentPage(text, currentPage)
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    isMeasured = true
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    loadCurrentPage(text, currentPage)
                }
            })
        }
    }

    /**
     * Set up the [OnActionListener]
     */
    fun setOnActionListener(listener: OnActionListener) {
        this.actionListener = listener
    }

    /**
     * Setting up a listener, which will receive swipe callbacks
     * @param [swipeListener] a listener which will receive swipe callbacks
     */
    fun setOnSwipeListener(swipeListener: OnSwipeListener) {
        this.swipeListener = swipeListener
    }

    private fun initPaginatedTextView() {
        movementMethod = SwipeableMovementMethod()
        highlightColor = Color.TRANSPARENT
    }

    private fun setPageState(pageState: ReadState) {
        this.text = pageState.pageText
        actionListener?.onPageLoaded(pageState)
        updateWordSpans()
    }

    private fun getSelectedWord(): String {
        return text.subSequence(selectionStart, selectionEnd).trim(' ').toString()
    }

    private fun loadCurrentPage(text: CharSequence, currentPage: Int) {
        val effectWidth = width - (paddingLeft + paddingRight)
        val effectHeight = height - (paddingTop + paddingBottom)
        controller = PaginationController(
            text,
            effectWidth,
            effectHeight,
            TextPaint(paint),
            lineSpacingMultiplier,
            lineSpacingExtra
        )
        controller.setCurrentPage(currentPage)
        setPageState(controller.getCurrentPage())
    }

    private fun updateWordSpans() {
        val spans = text as Spannable
        val spaceIndexes = text.trim().allWordPositions()
        var wordStart = 0
        var wordEnd: Int
        for (i in 0..spaceIndexes.size) {
            val swipeableSpan = createSwipeableSpan()
            wordEnd = if (i < spaceIndexes.size) spaceIndexes[i] else spans.length
            spans.setSpan(swipeableSpan, wordStart, wordEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            wordStart = wordEnd + 1
        }
    }

    private fun getSelectedParagraph(): String? {
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

    private fun createSwipeableSpan(): SwipeableSpan = object : SwipeableSpan() {

        override fun onClick(view: View) {
            getSelectedParagraph()?.takeIf { it.isNotEmpty() }
                ?.let { actionListener?.onClick(it) }
        }

        override fun onLongClick(view: View) {
            getSelectedWord().takeIf { it.isNotEmpty() }
                ?.let { actionListener?.onLongClick(it) }
        }

        override fun onSwipeLeft(view: View) {
            controller.getPrevPage()?.apply {
                setPageState(this)
                swipeListener?.onSwipeLeft()
            }
        }

        override fun onSwipeRight(view: View) {
            controller.getNextPage()?.apply {
                setPageState(this)
                swipeListener?.onSwipeRight()
            }
        }
    }
}