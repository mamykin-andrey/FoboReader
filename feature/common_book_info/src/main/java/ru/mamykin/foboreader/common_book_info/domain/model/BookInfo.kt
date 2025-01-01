package ru.mamykin.foboreader.common_book_info.domain.model

import java.io.File
import java.util.Date
import java.util.Locale

class BookInfo(
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
    val lastOpen: Long
) {
    private val file by lazy { File(filePath) }

    fun getFormat(): String {
        return file.extension.toUpperCase(Locale.getDefault())
    }

    fun getDisplayFileSize(): String {
        val fileSizeKb = file.length() / 1024
        return if (fileSizeKb > 1024) {
            "${fileSizeKb / 1024}MB"
        } else {
            "${fileSizeKb}KB"
        }
    }

    fun getReadPercent(): Float {
        if (totalPages == null) return 0f
        return ((currentPage + 1f) / totalPages) * 100f
    }

    fun containsText(text: String): Boolean {
        return title.contains(text, ignoreCase = true) || author.contains(text, ignoreCase = true)
    }
}