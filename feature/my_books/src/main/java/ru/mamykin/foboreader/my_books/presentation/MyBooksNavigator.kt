package ru.mamykin.foboreader.my_books.presentation

interface MyBooksNavigator {

    fun openBook(bookId: Long)

    fun openBookDetails(bookId: Long)
}