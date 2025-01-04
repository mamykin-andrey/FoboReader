package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

internal data class Book(
    val info: BookInfo,
    val pages: List<Page>,
    val userSettings: UserSettings,
) {
    data class Page(
        val sentences: List<String>,
        val translations: List<String>,
    )

    data class UserSettings(
        val fontSize: Int,
        val translationColorCode: String,
        val backgroundColorCode: String,
    )
}