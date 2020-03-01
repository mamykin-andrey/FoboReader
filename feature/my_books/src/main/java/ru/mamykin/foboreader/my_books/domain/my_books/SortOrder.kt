package ru.mamykin.foboreader.my_books.domain.my_books

sealed class SortOrder {
    object ByName : SortOrder()
    object ByReaded : SortOrder()
    object ByDate : SortOrder()
}