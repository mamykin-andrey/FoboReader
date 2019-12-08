package ru.mamykin.read_book.di

import org.koin.dsl.module
import ru.mamykin.read_book.data.YandexTranslateService

val readBookModule = module {

    single { YandexTranslateService.create() }
}