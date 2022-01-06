package ru.mamykin.foboreader.common_book_info.domain.model

import java.io.File
import java.util.*

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

    fun getFileSizeKb(): Long {
        return file.length() / 1024
    }

    fun getReadPercent(): Int {
        return totalPages?.takeIf { it > 0 }
            ?.toDouble()
            ?.let { currentPage / it * 100 }
            ?.toInt()
            ?: 0
    }

    fun containsText(text: String): Boolean {
        return title.contains(text, ignoreCase = true) || author.contains(text, ignoreCase = true)
    }
}