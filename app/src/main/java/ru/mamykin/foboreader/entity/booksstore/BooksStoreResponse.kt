package ru.mamykin.foboreader.entity.booksstore

import com.google.gson.annotations.SerializedName

data class BooksStoreResponse(
        @SerializedName("promotions")
        val promotions: List<StorePromotion>,
        @SerializedName("featured")
        val featured: List<StoreFeatured>,
        @SerializedName("categories")
        val categories: List<StoreCategory>
)