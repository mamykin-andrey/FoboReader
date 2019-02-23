package ru.mamykin.foboreader.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.core.di.qualifiers.BookPath
import ru.mamykin.foboreader.core.di.scope.ActivityScope
import ru.mamykin.foboreader.domain.readbook.TextToSpeechService

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