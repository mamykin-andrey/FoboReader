package ru.mamykin.foboreader.extension

import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import ru.mamykin.foboreader.entity.ViewParams

var View.isVisible: Boolean
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }
    get() = this.visibility == View.VISIBLE

@Suppress("deprecation")
fun TextView.addGlobalLayoutListener(callbackFunc: (ViewParams) -> Unit) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val viewParams: ViewParams

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                viewTreeObserver.removeGlobalOnLayoutListener(this)
                viewParams = ViewParams(width, height, paint, 1f, 0f, true)
            } else {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                viewParams = ViewParams(width, height, paint,
                        lineSpacingMultiplier, lineSpacingExtra, includeFontPadding)
            }
            callbackFunc(viewParams)
        }
    })
}