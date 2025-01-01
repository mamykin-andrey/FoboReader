package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.my_books.R
import javax.inject.Inject

internal class MyBookUIModelMapper @Inject constructor() {

    fun map(domainModel: BookInfo): MyBookUIModel {
        val totalPages = domainModel.totalPages
        val readStatus = if (totalPages != null) {
            StringOrResource.Resource(R.string.book_pages_info, domainModel.currentPage + 1, totalPages)
        } else {
            StringOrResource.Resource(R.string.book_pages_info_not_started)
        }
        val readPercent = if (totalPages != null) {
            domainModel.currentPage + 1f / totalPages
        } else {
            0f
        }
        return MyBookUIModel(
            id = domainModel.id,
            genre = domainModel.genre,
            coverUrl = domainModel.coverUrl,
            author = domainModel.author,
            title = domainModel.title,
            languages = domainModel.languages,
            readStatus = readStatus,
            lastOpen = domainModel.lastOpen,
            readPercent = readPercent,
        )
    }
}