package ru.mamykin.foboreader.domain.entity.booksstore

import com.google.gson.annotations.SerializedName

data class StoreCategory(
        @SerializedName("categoryId")
        val categoryId: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("subtitle")
        val subtitle: String,
        @SerializedName("pictureId")
        val pictureId: Int
)