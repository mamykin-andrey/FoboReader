package ru.mamykin.my_books.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.my_books.data.MyBooksRepository
import ru.mamykin.my_books.domain.MyBooksInteractor
import ru.mamykin.my_books.presentation.MyBooksViewModel

val myBooksModule = module {
    factory { MyBooksRepository(get(), get()) }
    factory { MyBooksInteractor(get()) }
    viewModel { MyBooksViewModel(get()) }
}