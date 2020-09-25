package ru.mamykin.foboreader.my_books.presentation.my_books

interface MyBooksNavigator {

    fun openBook(bookId: Long)

    fun openBookDetails(bookId: Long)
}