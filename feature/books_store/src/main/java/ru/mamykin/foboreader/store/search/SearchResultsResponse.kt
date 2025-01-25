package ru.mamykin.foboreader.store.search

import androidx.annotation.Keep
import ru.mamykin.foboreader.store.categories.BookCategoriesResponse
import ru.mamykin.foboreader.store.list.BookListResponse

@Keep
internal class SearchResultsResponse(
    val categories: List<BookCategoriesResponse.BookCategoryResponse>,
    val books: List<BookListResponse.BookResponse>,
)