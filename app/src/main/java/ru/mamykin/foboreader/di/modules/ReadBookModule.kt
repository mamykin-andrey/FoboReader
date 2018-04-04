package ru.mamykin.foboreader.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.di.qualifiers.BookPath
import ru.mamykin.foboreader.di.scope.ActivityScope
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