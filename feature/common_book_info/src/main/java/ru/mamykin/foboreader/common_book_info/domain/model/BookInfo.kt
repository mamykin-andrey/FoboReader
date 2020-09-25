package ru.mamykin.foboreader.common_book_info.domain.model

import java.io.File
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
    val lastOpen: Long
) {
    private val file by lazy { File(filePath) }

    fun getFormat(): String {
        return file.extension.toUpperCase(Locale.getDefault())
    }

    fun getFileSizeKb(): Long {
        return file.length() / 1024
    }
}