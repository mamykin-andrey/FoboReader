package ru.mamykin.store.domain.entity

import com.google.gson.annotations.SerializedName

data class StoreBook(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        var title: String,
        @SerializedName("author")
        var author: String,
        @SerializedName("authorId")
        var authorId: Int,
        @SerializedName("genre")
        var genre: String,
        @SerializedName("rating")
        var rating: Double,
        @SerializedName("pictureUrl")
        var pictureUrl: String
)