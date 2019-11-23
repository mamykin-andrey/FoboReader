package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.modules.ReadBookModule
import ru.mamykin.foboreader.core.di.scope.ActivityScope

@ActivityScope
@Subcomponent(modules = [ReadBookModule::class])
interface ReadBookComponent