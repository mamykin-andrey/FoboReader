package ru.mamykin.foboreader.my_books.di

import dagger.Subcomponent
import ru.mamykin.foboreader.my_books.presentation.MyBooksFragment

@Subcomponent
interface MyBooksComponent {

    fun inject(fragment: MyBooksFragment)
}