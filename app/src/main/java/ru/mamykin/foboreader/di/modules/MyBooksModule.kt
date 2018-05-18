package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.mybooks.MyBooksRouter

@Module
@FragmentScope
class MyBooksModule(
        private val router: MyBooksRouter
) {
    @Provides
    @FragmentScope
    fun provideRouter() = router
}