package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity

internal data class Book(
    val info: DownloadedBookEntity,
    val pages: List<Page>,
    val dictionary: Map<String, String> = emptyMap(),
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