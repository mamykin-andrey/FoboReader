package ru.mamykin.foboreader.common_book_info.di

import org.koin.dsl.module
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository

val commonBookInfoModule = module {
    single { DatabaseDependencies.dao(get()) }
    factory { BookInfoRepository(get()) }
}