package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBook

interface BookInfoParser {

    suspend fun parse(filePath: String): DownloadedBook?
}