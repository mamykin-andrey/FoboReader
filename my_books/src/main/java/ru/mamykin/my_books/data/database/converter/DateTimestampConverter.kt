package ru.mamykin.my_books.data.database.converter

import androidx.room.TypeConverter
import java.util.*

class DateTimestampConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun timestampToDate(timestamp: Long): Date = Date(timestamp)
}