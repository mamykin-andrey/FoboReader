package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.modules.MyBooksModule
import ru.mamykin.core.di.scope.FragmentScope

@FragmentScope
@Subcomponent(modules = [MyBooksModule::class])
interface MyBooksComponent