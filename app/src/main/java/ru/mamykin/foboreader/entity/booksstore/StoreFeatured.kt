package ru.mamykin.foboreader.entity.booksstore

import com.google.gson.annotations.SerializedName
import ru.mamykin.foboreader.entity.StoreBook

data class StoreFeatured(
        @SerializedName("title")
        val title: String,
        @SerializedName("subtitle")
        val subtitle: String,
        @SerializedName("categoryId")
        val categoryId: Int,
        @SerializedName("pictureId")
        val pictureId: Int,
        @SerializedName("books")
        val books: List<StoreBook>
)