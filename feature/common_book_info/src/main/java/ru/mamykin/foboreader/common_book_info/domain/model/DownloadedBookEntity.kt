package ru.mamykin.foboreader.common_book_info.domain.model

import java.util.Date

class DownloadedBookEntity(
    val id: Long,
    val filePath: String,
    val genre: String,
    val coverUrl: String?,
    val author: String,
    val title: String,
    val languages: List<String>,
    val date: Date?,
    val currentPage: Int,
    val totalPages: Int? = null,
    val lastOpen: Long?,
    val link: String,
) {
    fun containsText(text: String): Boolean {
        return title.contains(text, ignoreCase = true) || author.contains(text, ignoreCase = true)
    }

    fun isStarted(): Boolean {
        return lastOpen != null
    }
}