package ru.mamykin.foboreader.common_book_info.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import java.util.*

@Entity
class BookInfoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val filePath: String,
    val genre: String,
    val coverUrl: String?,
    val author: String,
    val title: String,
    val languages: List<String>,
    val date: Date?,
    val currentPage: Int,
    val totalPages: Int?,
    val lastOpen: Long
) {
    fun toDomainModel() = BookInfo(
        id = id,
        filePath = filePath,
        genre = genre,
        coverUrl = coverUrl,
        author = author,
        title = title,
        languages = languages,
        date = date,
        currentPage = currentPage,
        totalPages = totalPages,
        lastOpen = lastOpen
    )
}

fun BookInfo.toDatabaseModel() = BookInfoModel(
    id = id,
    filePath = filePath,
    genre = genre,
    coverUrl = coverUrl,
    author = author,
    title = title,
    languages = languages,
    date = date,
    currentPage = currentPage,
    totalPages = totalPages,
    lastOpen = lastOpen
)