package ru.mamykin.foboreader.entity

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
        @SerializedName("oldPrice")
        var oldPrice: String?,
        @SerializedName("price")
        var price: String,
        @SerializedName("pictureUrl")
        var pictureUrl: String
) {
    val ratingStr: String
        get() = rating.toString()

    val oldPriceStr: String
        get() = "$oldPrice Р"

    val priceStr: String
        get() = "$price Р"
}