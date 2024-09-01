package ru.mamykin.foboreader.book_details.details

data class BookDetails(
    val author: String,
    val title: String,
    val coverUrl: String?,
    val filePath: String,
    val currentPage: Int,
    val genre: String,
)