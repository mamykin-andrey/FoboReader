package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.di.qualifiers.BookId
import ru.mamykin.foboreader.di.scope.ActivityScope
import ru.mamykin.foboreader.ui.bookdetails.BookDetailsRouter

@ActivityScope
@Module
class BookDetailsModule(
        private val router: BookDetailsRouter,
        private val bookId: Int
) {

    @Provides
    @ActivityScope
    fun provideRouter(): BookDetailsRouter {
        return router
    }

    @BookId
    @Provides
    @ActivityScope
    fun provideBookId(): Int {
        return bookId
    }
}