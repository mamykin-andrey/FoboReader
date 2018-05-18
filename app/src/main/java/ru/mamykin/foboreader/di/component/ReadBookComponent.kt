package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.modules.ReadBookModule
import ru.mamykin.foboreader.di.scope.ActivityScope
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity

@ActivityScope
@Subcomponent(modules = [ReadBookModule::class])
interface ReadBookComponent {

    fun inject(activity: ReadBookActivity)
}