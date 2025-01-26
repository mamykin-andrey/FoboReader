package ru.mamykin.foboreader.store.list

internal data class StoreBookUIModel(
    val id: String,
    val genre: String,
    val author: String,
    val title: String,
    val languages: List<String>,
    val format: String,
    val cover: String,
    val link: String,
    val isOwned: Boolean,
    val rating: Float,
) {
    companion object {
        fun fromDomainModel(entity: StoreBookEntity) = StoreBookUIModel(
            id = entity.id,
            genre = entity.genre,
            author = entity.author,
            title = entity.title,
            languages = entity.languages,
            format = entity.format,
            cover = entity.coverUrl,
            link = entity.link,
            isOwned = entity.isOwned,
            rating = entity.rating,
        )
    }
}