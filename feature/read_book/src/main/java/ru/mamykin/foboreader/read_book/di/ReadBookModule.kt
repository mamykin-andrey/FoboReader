package ru.mamykin.foboreader.read_book.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.read_book.data.BookContentRepository
import ru.mamykin.foboreader.read_book.domain.helper.BookContentParser
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookContent
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookInfo
import ru.mamykin.foboreader.read_book.domain.usecase.GetParagraphTranslation
import ru.mamykin.foboreader.read_book.domain.usecase.UpdateBookInfo
import ru.mamykin.foboreader.read_book.presentation.ReadBookViewModel

val readBookModule = module {
    single { NetworkDependencies.service() }
    single { BookContentRepository(get()) }
    factory { BookContentParser() }
    factory { GetBookContent(get()) }
    factory { GetBookInfo(get()) }
    factory { GetParagraphTranslation(get()) }
    factory { UpdateBookInfo(get()) }
    viewModel { (bookId: Long) -> ReadBookViewModel(bookId, get(), get(), get(), get()) }
}