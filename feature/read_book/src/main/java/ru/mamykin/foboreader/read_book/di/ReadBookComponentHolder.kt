package ru.mamykin.foboreader.read_book.di

interface ReadBookComponentHolder {

    fun readBookComponent(bookId: Long): ReadBookComponent
}