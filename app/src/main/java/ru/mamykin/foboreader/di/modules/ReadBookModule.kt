package ru.mamykin.foboreader.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.core.di.qualifiers.BookPath
import ru.mamykin.core.di.scope.ActivityScope
import ru.mamykin.read_book.domain.TextToSpeechService

@ActivityScope
@Module
class ReadBookModule(
        private val bookPath: String
) {
    @BookPath
    @Provides
    @ActivityScope
    fun provideBookPath() = bookPath

    @Provides
    @ActivityScope
    fun provideTextToSpeechService(context: Context): TextToSpeechService {
        return TextToSpeechService(context)
    }
}