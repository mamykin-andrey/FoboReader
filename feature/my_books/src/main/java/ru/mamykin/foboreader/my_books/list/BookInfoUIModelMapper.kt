package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBook
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.my_books.R
import javax.inject.Inject

internal class BookInfoUIModelMapper @Inject constructor() {

    fun map(entity: DownloadedBook): BookInfoUIModel {
        val totalPages = entity.totalPages
        val readStatus = if (entity.isStarted()) {
            StringOrResource.Resource(R.string.book_pages_info, entity.currentPage + 1, totalPages!!)
        } else {
            StringOrResource.Resource(R.string.book_pages_info_not_started)
        }
        val readPercent = if (totalPages != null) {
            entity.currentPage + 1f / totalPages
        } else {
            0f
        }
        return BookInfoUIModel(
            id = entity.id,
            genre = entity.genre,
            coverUrl = entity.coverUrl,
            author = entity.author,
            title = entity.title,
            languages = entity.languages,
            readStatus = readStatus,
            readPercent = readPercent,
        )
    }
}