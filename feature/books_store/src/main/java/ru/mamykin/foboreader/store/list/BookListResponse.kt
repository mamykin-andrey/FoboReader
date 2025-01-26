package ru.mamykin.foboreader.store.list

import androidx.annotation.Keep

@Keep
internal class BookListResponse(
    val books: List<BookResponse>
) {
    @Keep
    internal class BookResponse(
        val id: String,
        val genre: String,
        val author: String,
        val title: String,
        val languages: List<String>,
        val format: String,
        val cover: String,
        val link: String,
        val rating: Float,
    ) {
        fun toDomainModel(isOwned: Boolean) = StoreBookEntity(
            id = id,
            genre = genre,
            author = author,
            title = title,
            languages = languages,
            format = format,
            coverUrl = cover,
            link = link,
            isOwned = isOwned,
            rating = rating,
        )
    }
}