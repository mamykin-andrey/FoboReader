package ru.mamykin.foboreader.data.database.converter

import android.arch.persistence.room.TypeConverter
import ru.mamykin.foboreader.data.database.BookDao

class SortOrderStringConverter {
    @TypeConverter
    fun sortOrderToString(sortOrder: BookDao.SortOrder): String = sortOrder.toString()
}