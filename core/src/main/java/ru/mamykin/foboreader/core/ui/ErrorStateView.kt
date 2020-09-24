package ru.mamykin.foboreader.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.mamykin.foboreader.core.R
import ru.mamykin.foboreader.core.extension.dpToPx

typealias OnRetryClickListener = () -> Unit

class ErrorStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var onRetryClickListener: OnRetryClickListener? = null

    init {
        gravity = Gravity.CENTER
        orientation = VERTICAL
        initViews()
    }

    fun setOnRetryClickListener(onRetryClickListener: OnRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener
    }

    private fun initViews() {
        addView(ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.ic_error_state)
        })
        addView(TextView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER_HORIZONTAL
            setText(R.string.core_error_retry_description)
        })
        addView(Button(context).apply {
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, context.dpToPx(8).toInt(), 0, 0)
                val paddingPx = context.dpToPx(16).toInt()
                setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
            }
            layoutParams = params
            background = null
            setText(R.string.core_error_retry_title)
            setTextColor(ContextCompat.getColor(context, R.color.retry_button_text))
            setOnClickListener { onRetryClicked() }
        })
    }

    private fun onRetryClicked() {
        onRetryClickListener?.invoke()
    }
}