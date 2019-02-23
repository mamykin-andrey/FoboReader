package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.modules.BookDetailsModule
import ru.mamykin.foboreader.core.di.scope.ActivityScope
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsActivity

@ActivityScope
@Subcomponent(modules = [BookDetailsModule::class])
interface BookDetailsComponent {

    fun inject(activity: BookDetailsActivity)
}