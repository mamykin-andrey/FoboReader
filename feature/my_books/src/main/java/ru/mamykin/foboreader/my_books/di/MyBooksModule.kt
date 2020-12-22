package ru.mamykin.foboreader.my_books.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.helper.BookFilesScanner
import ru.mamykin.foboreader.my_books.domain.helper.BookInfoParser
import ru.mamykin.foboreader.my_books.domain.usecase.*
import ru.mamykin.foboreader.my_books.presentation.MyBooksViewModel

val myBooksModule = module {
    factory { BookInfoParser() }
    factory { BookFilesScanner(androidContext(), get(), get()) }
    single { MyBooksRepository(get(), get(), androidContext()) }

    factory { ScanBooks(get()) }
    factory { GetMyBooks(get()) }
    factory { SortMyBooks(get()) }
    factory { FilterMyBooks(get()) }
    factory { RemoveBook(get()) }
    viewModel { MyBooksViewModel(get(), get(), get(), get(), get()) }
}