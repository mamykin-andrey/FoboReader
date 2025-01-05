package ru.mamykin.foboreader.store.categories

internal class BookCategoryEntity(
    val id: String,
    val name: String,
    val description: String?,
    val booksCount: Int,
)