package ru.mamykin.core.di

import androidx.room.Room
import org.koin.dsl.module
import ru.mamykin.core.data.BookParser
import ru.mamykin.core.data.PreferencesManager
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.data.database.BooksDatabase

val appModule = module {
    single { PreferencesManager(get()) }
    single { SettingsStorage(get()) }

    factory { BookParser() }

    single { Room.databaseBuilder(get(), BooksDatabase::class.java, "books").build() }
    single { get<BooksDatabase>().getBookDao() }
}