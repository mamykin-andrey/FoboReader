package ru.mamykin.my_books.domain.my_books

sealed class SortOrder {
    object ByName : SortOrder()
    object ByReaded : SortOrder()
    object ByDate : SortOrder()
}