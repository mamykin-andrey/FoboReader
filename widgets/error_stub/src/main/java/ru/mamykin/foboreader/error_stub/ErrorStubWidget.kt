package ru.mamykin.foboreader.error_stub

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class ErrorStubWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var retryClickListener: (() -> Unit)? = null

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_error_stub_content, this)
    }

    fun setRetryClickListener(retryClickListener: () -> Unit) {
        this.retryClickListener = retryClickListener
    }

    private fun onRetryClicked() {
        retryClickListener?.invoke()
    }
}