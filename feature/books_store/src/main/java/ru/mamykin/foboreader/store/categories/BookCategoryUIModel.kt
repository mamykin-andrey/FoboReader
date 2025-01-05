package ru.mamykin.foboreader.store.categories

internal data class BookCategoryUIModel(
    val id: String,
    val name: String,
    val description: String?,
    val booksCount: Int,
) {
    companion object {
        fun fromDomainModel(entity: BookCategoryEntity) = BookCategoryUIModel(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            booksCount = entity.booksCount,
        )
    }
}