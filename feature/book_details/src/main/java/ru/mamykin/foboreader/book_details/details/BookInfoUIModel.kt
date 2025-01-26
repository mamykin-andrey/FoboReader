package ru.mamykin.foboreader.book_details.details

import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity

internal data class BookInfoUIModel(
    val author: String,
    val title: String,
    val coverUrl: String?,
    val filePath: String,
    val currentPage: Int,
    val genre: String,
    val languages: List<String>,
) {
    companion object {
        fun fromDomainModel(entity: DownloadedBookEntity) = BookInfoUIModel(
            author = entity.author,
            title = entity.title,
            coverUrl = entity.coverUrl,
            filePath = entity.filePath,
            currentPage = entity.currentPage,
            genre = entity.genre,
            languages = entity.languages,
        )
    }
}