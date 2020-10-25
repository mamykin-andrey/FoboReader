package ru.mamykin.foboreader.my_books.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import ru.mamykin.foboreader.my_books.R

class LinearProgressView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {

    private val vFill: View
    private val vEmpty: View

    init {
        inflate(getContext(), R.layout.view_percent_progress, this)
        vFill = findViewById(R.id.v_fill)
        vEmpty = findViewById(R.id.v_empty)
    }

    fun setProgress(percentage: Int) {
        val percent = percentage / 100
        val leftPercent = 1.0 - percent
        vFill.layoutParams = LayoutParams(
            0,
            LayoutParams.MATCH_PARENT,
            percent.toFloat()
        )
        vEmpty.layoutParams = LayoutParams(
            0,
            LayoutParams.MATCH_PARENT,
            leftPercent.toFloat()
        )
    }
}