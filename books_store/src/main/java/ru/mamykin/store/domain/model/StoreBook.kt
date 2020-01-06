package ru.mamykin.store.domain.model

data class StoreBook(
        val genre: String,
        val author: String,
        val title: String,
        val lang: String,
        val link: String
)