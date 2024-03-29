package ru.mamykin.foboreader.my_books.domain.helper

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.domain.model.SortOrder

class BooksComparatorFactory {

    fun create(sortOrder: SortOrder): Comparator<BookInfo> = when (sortOrder) {
        SortOrder.ByName -> Comparator { o1, o2 -> o1.title.compareTo(o2.title) }
        SortOrder.ByReadPages -> Comparator { o1, o2 -> o1.currentPage.compareTo(o2.currentPage) }
        SortOrder.ByDate -> Comparator { o1, o2 -> o1.date?.compareTo(o2.date) ?: 0 }
    }
}