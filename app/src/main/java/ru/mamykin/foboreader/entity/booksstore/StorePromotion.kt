package ru.mamykin.foboreader.entity.booksstore

import com.google.gson.annotations.SerializedName

data class StorePromotion(
        @SerializedName("categoryId")
        val categoryId: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("subtitle")
        val subtitle: String,
        @SerializedName("pictureUrl")
        val pictureUrl: String
)