package ru.mamykin.foboreader.core.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.core.di.qualifiers.BookPath
import ru.mamykin.foboreader.core.di.scope.ActivityScope
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsRouter

@ActivityScope
@Module
class BookDetailsModule(
        private val router: BookDetailsRouter,
        private val bookPath: String
) {

    @Provides
    @ActivityScope
    fun provideRouter(): BookDetailsRouter {
        return router
    }

    @BookPath
    @Provides
    @ActivityScope
    fun provideBookId(): String {
        return bookPath
    }
}