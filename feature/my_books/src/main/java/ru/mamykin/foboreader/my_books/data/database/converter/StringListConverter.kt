package ru.mamykin.foboreader.my_books.data.database.converter

import androidx.room.TypeConverter

class StringListConverter {

    @TypeConverter
    fun stringToList(source: String): List<String> = source.split(",")

    @TypeConverter
    fun listToString(source: List<String>): String = source.joinToString(",")
}