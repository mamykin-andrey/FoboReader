package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.core.di.qualifiers.BookPath
import ru.mamykin.core.di.scope.ActivityScope

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