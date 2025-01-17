package ru.mamykin.foboreader.common_book_info.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import java.util.Date

@Entity
class DownloadedBookDBModel(
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
    val lastOpen: Long?,
) {
    fun toDomainModel() = DownloadedBookEntity(
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

    companion object {
        fun fromDomainModel(entity: DownloadedBookEntity) = DownloadedBookDBModel(
            id = entity.id,
            filePath = entity.filePath,
            genre = entity.genre,
            coverUrl = entity.coverUrl,
            author = entity.author,
            title = entity.title,
            languages = entity.languages,
            date = entity.date,
            currentPage = entity.currentPage,
            totalPages = entity.totalPages,
            lastOpen = entity.lastOpen,
        )
    }
}