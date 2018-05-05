package ru.mamykin.foboreader.entity.booksstore

import com.google.gson.annotations.SerializedName

data class BooksStoreResponse(
        @SerializedName("promotions")
        val promotions: List<PromotedCategory>,
        @SerializedName("featured")
        val featured: List<FeaturedCategory>,
        @SerializedName("categories")
        val categories: List<StoreCategory>
)