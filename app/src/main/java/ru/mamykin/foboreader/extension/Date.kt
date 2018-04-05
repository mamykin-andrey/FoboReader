package ru.mamykin.foboreader.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.asFormattedDate(): String? {
    return try {
        val format = SimpleDateFormat("MMM dd, yyyy", Locale("RU", "ru"))
        format.format(date)
    } catch (e: Exception) {
        null
    }
}