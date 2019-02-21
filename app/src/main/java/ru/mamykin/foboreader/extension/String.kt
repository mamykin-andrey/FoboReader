package ru.mamykin.foboreader.extension

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun CharSequence.allIndexesOf(char: Char): List<Int> {
    val indices = mutableListOf<Int>()
    this.forEachIndexed { index, currentChar ->
        if (currentChar == char) {
            indices.add(index)
        }
    }
    return indices
}

fun String.toDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    try {
        return format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return null
}