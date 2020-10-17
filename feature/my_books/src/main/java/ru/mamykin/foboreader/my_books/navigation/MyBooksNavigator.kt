package ru.mamykin.foboreader.my_books.navigation

interface MyBooksNavigator {

    fun myBooksToReadBook(bookId: Long)

    fun myBooksToBookDetails(bookId: Long)
}