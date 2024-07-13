package ru.mamykin.foboreader.core.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    return runCatching { format.parse(this) }.getOrNull()
}

fun String.trimSpecialCharacters(): String {
    // TODO: do not trim the whole text, trim only left and right sides
    return this.replace("[^a-zA-ZА-ЯЁа-яё0-9]".toRegex(), "")
}