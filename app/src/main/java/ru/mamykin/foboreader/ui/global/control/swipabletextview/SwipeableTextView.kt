package ru.mamykin.foboreader.ui.global.control.swipabletextview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.RectF
import android.text.Layout
import android.text.Spannable
import android.text.StaticLayout
import android.util.AttributeSet
import android.util.SparseIntArray
import android.util.TypedValue
import android.view.View
import ru.mamykin.foboreader.extension.allIndexesOf

/**
 * Элемент отображаюший текст книги, перевод и т д, поддерживает клики по абзацам,
 * лонг тапы по словам, горизонтальные свайпы, автоматическое масштабирование текста
 */
class SwipeableTextView : android.support.v7.widget.AppCompatTextView {

    private val textCachedSizes = SparseIntArray()
    private val availableSpaceRect = RectF()
    private var maxTextSize = textSize
    private val textRect = RectF()
    private var listener: OnActionListener? = null
    private var initializedDimens: Boolean = false
    private var widthLimit: Int = 0

//    private val selectedWord: String
//        get() = text.subSequence(selectionStart, selectionEnd).toString().trim { it <= ' ' }

    constructor(context: Context) : super(context) {
        initSwipeableTextView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initSwipeableTextView()
    }

    private fun initSwipeableTextView() {
        //movementMethod = SwipeableMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnSwipeListener(swipeListener: OnSwipeListener) {
        this.setOnTouchListener(SwipeDetector(swipeListener))
    }

    fun setOnActionListener(listener: OnActionListener) {
        this.listener = listener
    }

    override fun setTextSize(size: Float) {
        maxTextSize = size
        textCachedSizes.clear()
        adjustTextSize()
    }

    override fun setTextSize(unit: Int, size: Float) {
        val resources = context?.resources ?: Resources.getSystem()
        maxTextSize = TypedValue.applyDimension(unit, size, resources.displayMetrics)
        textCachedSizes.clear()
        adjustTextSize()
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        adjustTextSize()
    }

    private fun adjustTextSize() {
        if (initializedDimens) {
            val heightLimit = measuredHeight - compoundPaddingBottom - compoundPaddingTop
            widthLimit = measuredWidth - compoundPaddingLeft - compoundPaddingRight
            availableSpaceRect.right = widthLimit.toFloat()
            availableSpaceRect.bottom = heightLimit.toFloat()

            val textSize = efficientTextSizeSearch(20, maxTextSize.toInt(), availableSpaceRect)

            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        }
    }

    /**
     * Проверка того, что текст вмещается в необходимые размеры
     */
    private fun checkTextFits(suggestedSize: Int, availableRect: RectF): Boolean {
        paint.textSize = suggestedSize.toFloat()
        val layout = StaticLayout(
                text,
                paint,
                widthLimit,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                true
        )

        textRect.bottom = layout.height.toFloat()
        var maxWidth = -1
        for (i in 0..layout.lineCount) {
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
            if (checkTextFits(currentSize, availableSpace)) {
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

    /**
     * Установка swipable spannable на каждое слово в TextView
     */
    private fun updateWordsSpannables() {
        val spans = text as Spannable
        val spaceIndexes = text.trim().allIndexesOf(' ')
        var start = 0
        var end: Int
        for (i in 0..spaceIndexes.size) {
            end = if (i < spaceIndexes.size) spaceIndexes[i] else spans.length
            spans.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end + 1
        }
    }

//    private fun getSelectedParagraph(widget: TextView): String? {
//        val text = widget.text
//        val selStart = widget.selectionStart
//        val selEnd = widget.selectionEnd
//        var parStart: Int
//        var parEnd: Int
//        // Если кликнули по пустому параграфу
//        if (text.subSequence(selStart, selEnd).toString().contains("\n")) {
//            // Номер символа, с которого начинается нужный абзац
//            parStart = text.subSequence(0, selEnd).toString().lastIndexOf("\n")
//            parStart = if (parStart == -1) 0 else parStart
//            // Номер символа на котором кончается абзац
//            parEnd = text.subSequence(selEnd, text.length).toString().indexOf("\n")
//            parEnd = if (parEnd == -1) text.length else parEnd + selEnd
//        } else {
//            // Номер символа, с которого начинается нужный абзац
//            parStart = text.subSequence(0, selStart).toString().lastIndexOf("\n")
//            parStart = if (parStart == -1) 0 else parStart
//            // Номер символа на котором кончается абзац
//            parEnd = text.subSequence(selEnd, text.length).toString().indexOf("\n")
//            parEnd = if (parEnd == -1) text.length else parEnd + selEnd
//        }
//        return text.subSequence(parStart, parEnd).toString()
//    }

//    fun setTranslation(text: CharSequence) {
//        val spTrans = SpannableString(text)
//        spTrans.setSpan(
//                ForegroundColorSpan(Color.RED),
//                0,
//                spTrans.length,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        setText(TextUtils.concat(getText(), "\n\n", spTrans))
//        updateWordsSpannables()
//    }

    private val clickableSpan: SwipeableSpan = object : SwipeableSpan() {

        override fun onClick(widget: View) {
//                val paragraph = getSelectedParagraph(widget as TextView)
//                if (!TextUtils.isEmpty(paragraph) && listener != null) {
//                    listener!!.onClick(paragraph!!.trim { it <= ' ' })
//                }
        }

        override fun onLongClick(widget: View) {
//                val word = selectedWord
//                if (!TextUtils.isEmpty(word) && listener != null) {
//                    listener!!.onLongClick(word)
//                }
        }
    }
}