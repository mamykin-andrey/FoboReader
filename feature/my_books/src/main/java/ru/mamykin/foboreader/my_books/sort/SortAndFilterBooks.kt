package ru.mamykin.foboreader.my_books.sort

import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBook
import javax.inject.Inject

internal class SortAndFilterBooks @Inject constructor() {

    fun execute(allBooks: List<DownloadedBook>, sortOrder: SortOrder, searchQuery: String?): List<DownloadedBook> {
        val filteredBooks =
            if (searchQuery.isNullOrBlank()) allBooks else allBooks.filter { it.containsText(searchQuery) }
        return filteredBooks.sortedWith(BooksComparatorFactory().create(sortOrder))
    }
}