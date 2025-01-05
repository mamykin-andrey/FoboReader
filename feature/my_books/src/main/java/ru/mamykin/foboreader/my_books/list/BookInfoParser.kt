package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity

interface BookInfoParser {

    suspend fun parse(filePath: String): DownloadedBookEntity?
}