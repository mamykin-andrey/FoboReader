package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.mybooks.MyBooksFragment

@FragmentScope
@Subcomponent(modules = [])
interface MyBooksComponent {

    fun inject(fragment: MyBooksFragment)
}