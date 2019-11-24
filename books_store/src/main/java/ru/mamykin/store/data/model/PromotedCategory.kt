package ru.mamykin.store.data.model

import com.google.gson.annotations.SerializedName

data class PromotedCategory(
        @SerializedName("categoryId")
        val categoryId: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("subtitle")
        val subtitle: String,
        @SerializedName("pictureUrl")
        val pictureUrl: String
)