package ru.mamykin.foboreader.store.domain.model

data class StoreBookCategory(
    val id: String,
    val name: String,
    val description: String?,
    val booksCount: Int,
)