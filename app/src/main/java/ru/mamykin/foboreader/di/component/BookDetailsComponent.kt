package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.modules.BookDetailsModule
import ru.mamykin.foboreader.di.scope.ActivityScope
import ru.mamykin.foboreader.ui.bookdetails.BookDetailsActivity

@ActivityScope
@Subcomponent(modules = [BookDetailsModule::class])
interface BookDetailsComponent {

    fun inject(activity: BookDetailsActivity)
}