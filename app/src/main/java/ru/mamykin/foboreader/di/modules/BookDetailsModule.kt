package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.di.qualifiers.BookPath
import ru.mamykin.foboreader.di.scope.ActivityScope
import ru.mamykin.foboreader.ui.bookdetails.BookDetailsRouter

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