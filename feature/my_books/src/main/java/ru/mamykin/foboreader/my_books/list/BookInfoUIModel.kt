package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.core.presentation.StringOrResource

internal data class BookInfoUIModel(
    val id: Long,
    val genre: String,
    val coverUrl: String?,
    val author: String,
    val title: String,
    val languages: List<String>,
    val readStatus: StringOrResource,
    val readPercent: Float,
)