package ru.mamykin.foboreader.store.search

import ru.mamykin.foboreader.store.categories.BookCategory
import ru.mamykin.foboreader.store.list.StoreBook

internal data class StoreSearchModel(
    val categories: List<BookCategory>,
    val books: List<StoreBook>,
)