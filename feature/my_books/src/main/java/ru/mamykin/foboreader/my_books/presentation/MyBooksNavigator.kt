package ru.mamykin.foboreader.my_books.presentation

interface MyBooksNavigator {

    fun myBooksToReadBook(bookId: Long)

    fun myBooksToBookDetails(bookId: Long)
}