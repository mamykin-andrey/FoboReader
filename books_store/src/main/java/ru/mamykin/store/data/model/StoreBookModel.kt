package ru.mamykin.store.data.model

import com.google.gson.annotations.SerializedName
import ru.mamykin.store.domain.model.StoreBook

data class StoreBookModel(
        @SerializedName("genre")
        val genre: String,
        @SerializedName("author")
        val author: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("lang")
        val lang: String,
        @SerializedName("cover")
        val cover: String,
        @SerializedName("link")
        val link: String
) {
    fun toDomainModel() = StoreBook(
            genre = genre,
            author = author,
            title = title,
            lang = lang,
            cover = cover,
            link = link
    )
}