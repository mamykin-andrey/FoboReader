package ru.mamykin.foboreader.dictionary_impl

import androidx.room.TypeConverter
import java.util.Date

internal class DateTimestampConverter {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long = date?.time ?: 0

    @TypeConverter
    fun timestampToDate(timestamp: Long): Date? = timestamp
        .takeIf { it != 0L }
        ?.let { Date(it) }
}