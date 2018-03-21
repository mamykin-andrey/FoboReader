package ru.mamykin.foboreader.ui.global.control

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.RectF
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import ru.mamykin.foboreader.ui.global.widget.SwipeableMovementMethod
import ru.mamykin.foboreader.ui.global.widget.SwipeableSpan
import java.util.*

/**
 * Элемент отображаюший текст книги, перевод и т д, поддерживает клики по абзацам,
 * лонг тапы по словам, горизонтальные свайпы, автоматическое масштабирование текста
 */
class SwipeableTextView : android.support.v7.widget.AppCompatTextView {
    private val textCachedSizes = SparseIntArray()
    private val availableSpaceRect = RectF()
    private var maxTextSize = textSize
    private val textRect = RectF()
    private var listener: SwipeableListener? = null
    private var initializedDimens: Boolean = false
    private var widthLimit: Int = 0

    private val selectedWord: String
        get() = text.subSequence(selectionStart, selectionEnd).toString().trim { it <= ' ' }

    private val clickableSpan: SwipeableSpan
        get() = object : SwipeableSpan() {
            override fun onClick(widget: View) {
                val paragraph = getSelectedParagraph(widget as TextView)
                if (!TextUtils.isEmpty(paragraph) && listener != null) {
                    listener!!.onClick(paragraph!!.trim { it <= ' ' })
                }
            }

            override fun onLongClick(widget: View) {
                val word = selectedWord
                if (!TextUtils.isEmpty(word) && listener != null) {
                    listener!!.onLongClick(word)
                }
            }

            override fun onSwipeLeft(view: View) {
                listener!!.onSwipeLeft()
            }

            override fun onSwipeRight(view: View) {
                listener!!.onSwipeRight()
            }

            override fun updateDrawState(ds: TextPaint) {}
        }

    constructor(context: Context) : super(context) {

        initSwipeableTextView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        initSwipeableTextView()
    }

    private fun initSwipeableTextView() {
        movementMethod = SwipeableMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    fun setSwipeableListener(listener: SwipeableListener) {
        this.listener = listener
    }

    override fun setTextSize(size: Float) {
        maxTextSize = size
        textCachedSizes.clear()
        adjustTextSize()
    }

    override fun setTextSize(unit: Int, size: Float) {
        val c = context
        val r: Resources

        r = if (c == null) Resources.getSystem() else c.resources
        maxTextSize = TypedValue.applyDimension(unit, size, r.displayMetrics)
        textCachedSizes.clear()
        adjustTextSize()
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        adjustTextSize()
    }

    private fun adjustTextSize() {
        if (!initializedDimens)
            return

        val heightLimit = measuredHeight - compoundPaddingBottom - compoundPaddingTop
        widthLimit = measuredWidth - compoundPaddingLeft - compoundPaddingRight
        availableSpaceRect.right = widthLimit.toFloat()
        availableSpaceRect.bottom = heightLimit.toFloat()
        val textSize = efficientTextSizeSearch(20, maxTextSize.toInt(), availableSpaceRect)

        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    fun checkSize(suggestedSize: Int, availableRect: RectF): Boolean {
        paint.textSize = suggestedSize.toFloat()
        val text = text.toString()
        val spacingMult = 1.0f
        val spacingAdd = 0.0f
        val layout = StaticLayout(text, paint, widthLimit,
                Layout.Alignment.ALIGN_NORMAL, spacingMult, spacingAdd, true)

        textRect.bottom = layout.height.toFloat()
        var maxWidth = -1
        for (i in 0 until layout.lineCount) {
            if (maxWidth < layout.getLineWidth(i)) {
                maxWidth = layout.getLineWidth(i).toInt()
            }
        }
        textRect.right = maxWidth.toFloat()
        return containsRectF(availableRect, textRect)
    }

    private fun containsRectF(containerRect: RectF, actualRect: RectF): Boolean {
        containerRect.offset(0f, 0f)
        val aArea = containerRect.width() * containerRect.height()
        actualRect.offset(0f, 0f)
        val bArea = actualRect.width() * actualRect.height()
        return aArea >= bArea
    }

    private fun efficientTextSizeSearch(start: Int, end: Int, availableSpace: RectF): Int {
        val key = text.toString().length
        val size = textCachedSizes.get(key)
        if (size != 0) {
            return size
        }
        textCachedSizes.put(key, size)
        return binarySearch(start, end, availableSpace)
    }

    private fun binarySearch(start: Int, end: Int, availableSpace: RectF): Int {
        var lastBest = start
        var lowSize = start
        var highSize = end - 1
        var currentSize: Int
        while (lowSize <= highSize) {
            currentSize = (lowSize + highSize).ushr(1)
            if (checkSize(currentSize, availableSpace)) {
                // Нормальный текст/маленький
                lastBest = currentSize
                lowSize = currentSize + 1
            } else {
                // Слишком большой текст
                highSize = currentSize - 1
                lastBest = lowSize
            }
        }
        return lastBest
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        initializedDimens = true
        textCachedSizes.clear()
        if (width != oldWidth || height != oldHeight) {
            adjustTextSize()
        }
    }

    fun updateWordLinks() {
        val spans = text as Spannable
        val indices = getIndices(text.toString().trim { it <= ' ' }, ' ')
        var start = 0
        var end: Int
        for (i in 0..indices.size) {
            val clickSpan = clickableSpan
            end = if (i < indices.size) indices[i] else spans.length
            spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end + 1
        }
    }

    private fun getSelectedParagraph(widget: TextView): String? {
        val text = widget.text
        val selStart = widget.selectionStart
        val selEnd = widget.selectionEnd
        var parStart: Int
        var parEnd: Int
        // Если кликнули по пустому параграфу
        if (text.subSequence(selStart, selEnd).toString().contains("\n")) {
            // Номер символа, с которого начинается нужный абзац
            parStart = text.subSequence(0, selEnd).toString().lastIndexOf("\n")
            parStart = if (parStart == -1) 0 else parStart
            // Номер символа на котором кончается абзац
            parEnd = text.subSequence(selEnd, text.length).toString().indexOf("\n")
            parEnd = if (parEnd == -1) text.length else parEnd + selEnd
        } else {
            // Номер символа, с которого начинается нужный абзац
            parStart = text.subSequence(0, selStart).toString().lastIndexOf("\n")
            parStart = if (parStart == -1) 0 else parStart
            // Номер символа на котором кончается абзац
            parEnd = text.subSequence(selEnd, text.length).toString().indexOf("\n")
            parEnd = if (parEnd == -1) text.length else parEnd + selEnd
        }
        return text.subSequence(parStart, parEnd).toString()
    }

    fun setTranslation(text: CharSequence) {
        val spTrans = SpannableString(text)
        spTrans.setSpan(ForegroundColorSpan(
                Color.RED), 0, spTrans.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        setText(TextUtils.concat(getText(), "\n\n", spTrans))
        updateWordLinks()
    }

    private fun getIndices(s: String, c: Char): Array<Int> {
        var pos = s.indexOf(c, 0)
        val indices = ArrayList<Int>()
        while (pos != -1) {
            indices.add(pos)
            pos = s.indexOf(c, pos + 1)
        }
        return indices.toTypedArray<Int>()
    }

    interface SwipeableListener {
        fun onClick(paragraph: String)

        fun onLongClick(word: String)

        //        void onPageLoaded();

        fun onSwipeLeft()

        fun onSwipeRight()
    }
}