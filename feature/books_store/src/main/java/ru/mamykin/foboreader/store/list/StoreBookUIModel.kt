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
    val ownedState: OwnedState,
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
            ownedState = if (entity.isOwned) OwnedState.Owned else OwnedState.NotOwned,
            rating = entity.rating,
        )
    }
}

internal sealed class OwnedState {
    data object Owned : OwnedState()
    data object Downloading : OwnedState()
    data object NotOwned : OwnedState()
}