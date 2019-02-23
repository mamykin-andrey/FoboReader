package ru.mamykin.foboreader.core.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.core.di.qualifiers.BookPath
import ru.mamykin.foboreader.core.di.scope.ActivityScope

@ActivityScope
@Module
class BookDetailsModule(
        private val bookPath: String
) {
    @BookPath
    @Provides
    @ActivityScope
    fun provideBookId(): String = bookPath
}