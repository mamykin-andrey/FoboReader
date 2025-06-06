package ru.mamykin.foboreader.book_details.details

import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBook

internal data class BookInfoUIModel(
    val id: Long,
    val author: String,
    val title: String,
    val coverUrl: String?,
    val filePath: String,
    val currentPage: Int,
    val genre: String,
    val languages: List<String>,
    val readPercent: Float,
    val rating: Float,
    val isRatedByUser: Boolean,
) {
    companion object {
        fun fromDomainModel(entity: DownloadedBook): BookInfoUIModel {
            val readPercent = entity.totalPages?.let { entity.currentPage + 1f / it } ?: 0f
            return BookInfoUIModel(
                id = entity.id,
                author = entity.author,
                title = entity.title,
                coverUrl = entity.coverUrl,
                filePath = entity.filePath,
                currentPage = entity.currentPage,
                genre = entity.genre,
                languages = entity.languages,
                readPercent = readPercent,
                rating = entity.rating,
                isRatedByUser = entity.isRatedByUser,
            )
        }
    }
}