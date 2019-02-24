package ru.mamykin.foboreader.core.extension

import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.domain.entity.ViewParams

var View.isVisible: Boolean
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }
    get() = this.visibility == View.VISIBLE

@Suppress("deprecation")
fun TextView.addGlobalLayoutListener(callbackFunc: (ViewParams) -> Unit) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        @SuppressLint("ObsoleteSdkInt")
        override fun onGlobalLayout() {
            val viewParams: ViewParams = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                viewTreeObserver.removeGlobalOnLayoutListener(this)
                ViewParams(width, height, paint, 1f, 0f, true)
            } else {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                ViewParams(width, height, paint,
                        lineSpacingMultiplier, lineSpacingExtra, includeFontPadding)
            }
            callbackFunc(viewParams)
        }
    })
}

fun ViewGroup.inflateView(@LayoutRes resId: Int, attach: Boolean = false) =
        LayoutInflater.from(this.context).inflate(R.layout.item_promoted_category, this, attach)