package ru.mamykin.foboreader.read_book.di

import org.koin.dsl.module

val readBookModule = module {
    single { NetworkDependencies.service() }
}