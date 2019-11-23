package ru.mamykin.foboreader.data.model

import com.google.gson.annotations.SerializedName
import ru.mamykin.foboreader.domain.entity.StoreBook

data class FeaturedCategory(
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