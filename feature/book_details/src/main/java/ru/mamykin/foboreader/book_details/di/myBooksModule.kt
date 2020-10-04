package ru.mamykin.foboreader.book_details.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.book_details.domain.interactor.BookDetailsInteractor
import ru.mamykin.foboreader.book_details.presentation.BookDetailsViewModel

val bookDetailsModule = module {
    factory { BookDetailsInteractor(get()) }
    viewModel { (bookId: Long) ->
        BookDetailsViewModel(
            bookId,
            get(),
            get()
        )
    }
}