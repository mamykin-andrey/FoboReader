package ru.mamykin.foboreader.book_details.di

interface BookDetailsComponentHolder {

    fun bookDetailsComponent(bookId: Long): BookDetailsComponent
}