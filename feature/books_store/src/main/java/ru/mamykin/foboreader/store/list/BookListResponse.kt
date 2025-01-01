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
        @Deprecated("Remove it")
        val format: String,
        val cover: String,
        val link: String
    ) {
        fun toDomainModel() = StoreBook(
            id = id,
            genre = genre,
            author = author,
            title = title,
            languages = languages,
            format = format,
            cover = cover,
            link = link
        )
    }
}