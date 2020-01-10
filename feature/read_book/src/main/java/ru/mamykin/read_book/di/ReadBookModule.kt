package ru.mamykin.read_book.di

import org.koin.dsl.module

val readBookModule = module {
    single { NetworkDependencies.service() }
}