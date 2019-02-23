package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.scope.FragmentScope
import ru.mamykin.foboreader.presentation.booksstore.BooksStoreFragment

@FragmentScope
@Subcomponent(modules = [])
interface BooksStoreComponent {

    fun inject(fragment: BooksStoreFragment)
}