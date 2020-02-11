package ru.mamykin.core.extension

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import ru.mamykin.core.platform.ViewParams

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
        LayoutInflater.from(this.context).inflate(resId, this, attach)

fun SeekBar.setOnSeekBarChangeListener(callback: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) callback(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
}

fun View.showPopupMenu(@MenuRes menuRes: Int, vararg clicks: Pair<Int, () -> Unit>) {
    PopupMenu(context, this).apply {
        setOnMenuItemClickListener {
            clicks.find { (id, _) -> it.itemId == id }?.second?.invoke()
            return@setOnMenuItemClickListener true
        }
        inflate(menuRes)
        show()
    }
}