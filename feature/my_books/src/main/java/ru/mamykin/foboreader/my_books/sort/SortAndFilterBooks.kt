package ru.mamykin.foboreader.my_books.sort

import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import javax.inject.Inject

internal class SortAndFilterBooks @Inject constructor() {

    fun execute(allBooks: List<DownloadedBookEntity>, sortOrder: SortOrder, searchQuery: String?): List<DownloadedBookEntity> {
        val filteredBooks =
            if (searchQuery.isNullOrBlank()) allBooks else allBooks.filter { it.containsText(searchQuery) }
        return filteredBooks.sortedWith(BooksComparatorFactory().create(sortOrder))
    }
}