package ru.mamykin.core.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    try {
        return format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return null
}

fun String.transliterate(): String {
    return StringTransliterator.transliterate(this)
}