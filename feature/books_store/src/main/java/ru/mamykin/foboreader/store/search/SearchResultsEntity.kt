package ru.mamykin.foboreader.store.search

import ru.mamykin.foboreader.store.categories.BookCategoryEntity
import ru.mamykin.foboreader.store.list.StoreBookEntity

internal data class SearchResultsEntity(
    val categories: List<BookCategoryEntity>,
    val books: List<StoreBookEntity>,
)