package ru.mamykin.foboreader.presentation.mybooks

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_percent_progress.view.*
import ru.mamykin.foboreader.R

/**
 * Элемент отображающий горизонтальную полосу прогресса
 */
class PercentProgressView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    init {
        inflate(getContext(), R.layout.view_percent_progress, this)
    }

    fun setPercentage(percents: Float) {
        val paramsFill = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, percents / 100f)
        vFill.layoutParams = paramsFill

        val paramsEmpty = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, (100 - percents) / 100f)
        vEmpty.layoutParams = paramsEmpty
    }
}