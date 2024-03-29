package ru.mamykin.foboreader.my_books.domain.model

sealed class SortOrder {
    object ByName : SortOrder()
    object ByReadPages : SortOrder()
    object ByDate : SortOrder()
}