package ru.mamykin.foboreader.book_details.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.book_details.domain.usecase.GetBookDetails
import ru.mamykin.foboreader.book_details.presentation.BookDetailsViewModel

val bookDetailsModule = module {
    factory { GetBookDetails(get()) }
    viewModel { (bookId: Long) ->
        BookDetailsViewModel(
            bookId,
            get(),
            get()
        )
    }
}