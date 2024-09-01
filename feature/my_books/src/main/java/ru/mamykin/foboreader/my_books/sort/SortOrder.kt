package ru.mamykin.foboreader.my_books.sort

sealed class SortOrder {
    object ByName : SortOrder()
    object ByReadPages : SortOrder()
    object ByDate : SortOrder()
}