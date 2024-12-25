package ru.mamykin.foboreader.store.list

data class StoreBook(
    val id: String,
    val genre: String,
    val author: String,
    val title: String,
    val lang: String,
    val format: String,
    val cover: String,
    val link: String
)