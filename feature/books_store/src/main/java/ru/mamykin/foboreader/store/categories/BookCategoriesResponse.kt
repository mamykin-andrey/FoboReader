package ru.mamykin.foboreader.store.categories

import androidx.annotation.Keep

@Keep
internal class BookCategoriesResponse(
    val categories: List<BookCategoryResponse>
) {
    @Keep
    internal class BookCategoryResponse(
        val id: String,
        val name: String,
        val description: String?,
        val booksCount: Int,
    ) {
        fun toDomainModel() = BookCategory(
            id = id,
            name = name,
            description = description,
            booksCount = booksCount
        )
    }
}