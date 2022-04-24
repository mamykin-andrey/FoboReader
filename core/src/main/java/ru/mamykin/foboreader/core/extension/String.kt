package ru.mamykin.foboreader.core.extension

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    return runCatching { format.parse(this) }.getOrNull()
}

fun SpannableString.setColor(color: Int, start: Int = 0, end: Int = length - 1) {
    setSpan(
        ForegroundColorSpan(color),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

@Suppress("deprecation")
fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, 0)
    } else {
        Html.fromHtml(this)
    }
}

fun String.trimSpecialCharacters(): String {
    // TODO: do not trim the whole text, trim only left and right sides
    return this.replace("[^a-zA-ZА-ЯЁа-яё0-9]".toRegex(), "")
}

fun CharSequence.allWordPositions(): List<Int> {
    fun isWordDelimiter(char: Char) = char == ' ' || char == '\n'
    val indexes = mutableListOf<Int>()
    for (i in this.indices) {
        if (isWordDelimiter(this[i])) indexes.add(i)
    }
    return indexes
}