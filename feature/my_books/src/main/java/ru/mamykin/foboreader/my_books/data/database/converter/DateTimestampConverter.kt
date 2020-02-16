package ru.mamykin.foboreader.my_books.data.database.converter

import androidx.room.TypeConverter
import java.util.*

class DateTimestampConverter {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long = date?.time ?: 0

    @TypeConverter
    fun timestampToDate(timestamp: Long): Date? = timestamp
            .takeIf { it != 0L }
            ?.let { Date(it) }
}