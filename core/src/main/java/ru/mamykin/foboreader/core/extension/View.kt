package ru.mamykin.foboreader.core.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.SeekBar
import android.widget.Switch

fun SeekBar.setProgressChangedListener(listener: (Int) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                listener.invoke(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    })
}

fun Switch.setCheckedChangedListener(listener: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        listener.invoke(isChecked)
    }
}

fun ViewGroup.getLayoutInflater(): LayoutInflater = LayoutInflater.from(context)

fun View.addGlobalLayoutListener(onGlobalLayout: (View) -> Unit) {
    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            onGlobalLayout(this@addGlobalLayoutListener)
        }
    }
    viewTreeObserver.addOnGlobalLayoutListener(listener)
}