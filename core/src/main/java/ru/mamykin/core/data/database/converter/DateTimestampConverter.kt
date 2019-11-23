package ru.mamykin.core.data.database.converter

import androidx.room.TypeConverter
import java.util.*

class DateTimestampConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun timestampToDare(timestamp: Long): Date = Date(timestamp)
}