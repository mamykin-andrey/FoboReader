package ru.mamykin.foboreader.core.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.TypedValue

fun Context.dpToPx(value: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        resources.displayMetrics
    )
}

fun Context.getActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Called on an object which isn't Activity or ContextWrapper: $this!")
}