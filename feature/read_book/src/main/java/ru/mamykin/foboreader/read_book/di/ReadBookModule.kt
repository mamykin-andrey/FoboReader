package ru.mamykin.foboreader.read_book.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.mamykin.foboreader.read_book.translation.TranslationRepository
import ru.mamykin.foboreader.read_book.translation.google.GoogleTranslationRepository

@Module
@InstallIn(ViewModelComponent::class)
internal interface BooksStoreBindsModule {

    @Binds
    fun bindFileRepository(impl: GoogleTranslationRepository): TranslationRepository
}