package ru.mamykin.foboreader.my_books.data

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.domain.helper.BookFilesScanner
import ru.mamykin.foboreader.my_books.domain.helper.BooksComparatorFactory
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import javax.inject.Inject

internal class MyBooksRepository @Inject constructor(
    private val repository: BookInfoRepository,
    private val booksScanner: BookFilesScanner,
) {
    private var allBooks: List<BookInfo>? = null
    private var sortOrder: SortOrder = SortOrder.ByName
    private var searchQuery: String = ""

    suspend fun getBooks(force: Boolean): List<BookInfo> {
        return sortAndFilter(
            books = getAllBooks(force),
            searchQuery = searchQuery,
            sortOrder = sortOrder
        )
    }

    private suspend fun getAllBooks(force: Boolean): List<BookInfo> {
        return allBooks.takeIf { !force } ?: loadBooks()
            .also { allBooks = it }
    }

    private suspend fun loadBooks(): List<BookInfo> {
        booksScanner.scan()
        return repository.getBooks()
    }

    private fun sortAndFilter(
        books: List<BookInfo>,
        searchQuery: String,
        sortOrder: SortOrder
    ): List<BookInfo> {
        val filteredBooks = if (searchQuery.isBlank()) books else books.filter { it.containsText(searchQuery) }
        return filteredBooks.sortedWith(BooksComparatorFactory().create(sortOrder))
    }

    suspend fun sortBooks(sortOrder: SortOrder): List<BookInfo> {
        this.sortOrder = sortOrder
        return getBooks(false)
    }

    suspend fun filterBooks(searchQuery: String): List<BookInfo> {
        this.searchQuery = searchQuery
        return getBooks(false)
    }
}