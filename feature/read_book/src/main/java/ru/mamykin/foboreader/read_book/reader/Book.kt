package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

internal data class Book(
    val info: BookInfo,
    val content: BookContent,
    val pages: List<String>,
)