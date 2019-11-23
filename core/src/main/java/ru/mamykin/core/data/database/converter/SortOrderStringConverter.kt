package ru.mamykin.core.data.database.converter

import androidx.room.TypeConverter
import ru.mamykin.core.data.database.BookDao

class SortOrderStringConverter {
    @TypeConverter
    fun sortOrderToString(sortOrder: BookDao.SortOrder): String = sortOrder.toString()
}