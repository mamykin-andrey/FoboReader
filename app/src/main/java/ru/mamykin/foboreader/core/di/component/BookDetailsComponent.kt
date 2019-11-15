package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.modules.BookDetailsModule
import ru.mamykin.foboreader.core.di.scope.ActivityScope

@ActivityScope
@Subcomponent(modules = [BookDetailsModule::class])
interface BookDetailsComponent {
}