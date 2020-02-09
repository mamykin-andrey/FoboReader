package ru.mamykin.my_books.domain.model

import java.util.*

data class BookInfo(
        val id: Long,
        val filePath: String,
        val genre: String,
        val coverUrl: String?,
        val author: String,
        val title: String,
        val languages: List<String>,
        val date: Date?,
        val currentPage: Int,
        val lastOpen: Long,
        val format: Int
)