package ru.mamykin.my_books.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mamykin.my_books.domain.model.BookInfo
import java.util.*

@Entity
data class BookInfoModel(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val filePath: String,
        val genre: String,
        val coverUrl: String,
        val author: String,
        val title: String,
        val languages: List<String>,
        val date: Date,
        val currentPage: Int,
        val lastOpen: Long,
        val format: Int
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
            lastOpen = lastOpen,
            format = format
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
        lastOpen = lastOpen,
        format = format
)