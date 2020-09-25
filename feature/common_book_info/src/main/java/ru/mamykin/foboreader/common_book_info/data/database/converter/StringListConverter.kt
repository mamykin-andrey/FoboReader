package ru.mamykin.foboreader.common_book_info.data.database.converter

import androidx.room.TypeConverter

class StringListConverter {

    @TypeConverter
    fun stringToList(source: String): List<String> = source.split(",")

    @TypeConverter
    fun listToString(source: List<String>): String = source.joinToString(",")
}