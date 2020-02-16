package ru.mamykin.foboreader.core.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.asFormattedDate(): String? {
    return try {
        SimpleDateFormat("MMM dd, yyyy", Locale("RU", "ru")).format(date)
    } catch (e: Exception) {
        null
    }
}