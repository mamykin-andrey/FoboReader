package ru.mamykin.foboreader.core.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.core.di.scope.FragmentScope
import ru.mamykin.foboreader.presentation.mybooks.MyBooksRouter

@Module
@FragmentScope
class MyBooksModule(
        private val router: MyBooksRouter
) {
    @Provides
    @FragmentScope
    fun provideRouter() = router
}