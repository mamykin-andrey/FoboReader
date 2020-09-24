package ru.mamykin.foboreader.core.extension

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    return runCatching { format.parse(this) }.getOrNull()
}

fun SpannableString.setColor(color: Int, start: Int, end: Int) {
    setSpan(
        ForegroundColorSpan(color),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}