package ru.mamykin.foboreader.core.domain.model

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

    fun getFormat(): String = file.extension.toUpperCase()

    fun getSize(): String {
        var size = file.length() / 1024
        var unit = "KB"
        if (size > 1000) {
            size /= 1024
            unit = "MB"
        }
        return "$size $unit"
    }
}