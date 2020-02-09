package ru.mamykin.core.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    return runCatching { format.parse(this) }.getOrNull()
}

fun String.transliterate(): String {
    return StringTransliterator.transliterate(this)
}