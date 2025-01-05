package ru.mamykin.foboreader.store.list

internal data class StoreBookEntity(
    val id: String,
    val genre: String,
    val author: String,
    val title: String,
    val languages: List<String>,
    val format: String,
    val coverUrl: String,
    val link: String,
)