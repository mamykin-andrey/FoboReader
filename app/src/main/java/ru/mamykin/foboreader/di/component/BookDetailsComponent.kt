package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.modules.BookDetailsModule
import ru.mamykin.core.di.scope.ActivityScope

@ActivityScope
@Subcomponent(modules = [BookDetailsModule::class])
interface BookDetailsComponent