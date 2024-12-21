package ru.mamykin.foboreader.my_books.sort

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import javax.inject.Inject

internal class SortAndFilterBooks @Inject constructor() {

    fun execute(allBooks: List<BookInfo>, sortOrder: SortOrder, searchQuery: String?): List<BookInfo> {
        val filteredBooks =
            if (searchQuery.isNullOrBlank()) allBooks else allBooks.filter { it.containsText(searchQuery) }
        return filteredBooks.sortedWith(BooksComparatorFactory().create(sortOrder))
    }
}