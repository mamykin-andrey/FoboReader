package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.mybooks.MyBooksFragment
import ru.mamykin.foboreader.ui.mybooks.MyBooksRouter

@FragmentScope
@Subcomponent(modules = [MyBooksRouter::class])
interface MyBooksComponent {

    fun inject(fragment: MyBooksFragment)
}