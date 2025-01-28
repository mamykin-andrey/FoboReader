package ru.mamykin.foboreader.common_book_info.domain.model

import java.util.Date

data class DownloadedBookEntity(
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
    val rating: Float,
    val isRatedByUser: Boolean,
) {
    fun containsText(text: String): Boolean {
        return title.contains(text, ignoreCase = true) || author.contains(text, ignoreCase = true)
    }

    fun isStarted(): Boolean {
        return totalPages != null
    }
}