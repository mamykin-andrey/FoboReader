package ru.mamykin.foboreader.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.service.TextToSpeechService
import ru.mamykin.foboreader.di.qualifiers.BookId
import ru.mamykin.foboreader.di.qualifiers.BookPath
import ru.mamykin.foboreader.di.scope.ActivityScope

@ActivityScope
@Module
class ReadBookModule(
        private val bookId: Int?,
        private val bookPath: String?
) {

    @BookId
    @Provides
    @ActivityScope
    fun provideBookId() = bookId

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