package ru.mamykin.foboreader.entity

data class BooksStoreResponse(
        val promotions: List<Promotion>,
        val featured: List<Featured>,
        val categories: List<StoreCategory>
)

data class Promotion(
        val categoryId: Int,
        val title: String,
        val subtitle: String,
        val pictureUrl: String
)

data class Featured(
        val title: String,
        val subtitle: String,
        val categoryId: Int,
        val pictureId: Int,
        val books: List<StoreBook>
)

data class StoreCategory(
        val categoryId: Int,
        val title: String,
        val subtitle: String,
        val pictureId: Int
)