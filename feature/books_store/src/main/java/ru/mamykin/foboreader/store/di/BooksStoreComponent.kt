package ru.mamykin.foboreader.store.di

import dagger.Subcomponent
import ru.mamykin.foboreader.store.presentation.BooksStoreFragment

@Subcomponent(modules = [BooksStoreModule::class])
interface BooksStoreComponent {

    fun inject(fragment: BooksStoreFragment)
}