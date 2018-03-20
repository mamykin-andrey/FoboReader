package ru.mamykin.foreignbooksreader.ui.controls

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

import butterknife.BindView
import butterknife.ButterKnife
import ru.mamykin.foreignbooksreader.R

/**
 * Элемент отображающий горизонтальную полосу прогресса
 */
class PercentProgressView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    @BindView(R.id.vFill)
    protected var vFill: View? = null
    @BindView(R.id.vEmpty)
    protected var vEmpty: View? = null

    init {
        View.inflate(getContext(), R.layout.view_percent_progress, this)
        ButterKnife.bind(this)
    }

    fun setPercents(percents: Float) {
        val paramsFill = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, percents / 100f)
        vFill!!.layoutParams = paramsFill

        val paramsEmpty = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, (100 - percents) / 100f)
        vEmpty!!.layoutParams = paramsEmpty
    }
}