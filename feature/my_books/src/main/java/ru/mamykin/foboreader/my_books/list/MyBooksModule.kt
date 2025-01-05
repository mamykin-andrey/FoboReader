package ru.mamykin.foboreader.my_books.list

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface MyBooksBindsModule {

    @Binds
    fun bindBookParser(parser: JsonBookInfoParser): BookInfoParser
}