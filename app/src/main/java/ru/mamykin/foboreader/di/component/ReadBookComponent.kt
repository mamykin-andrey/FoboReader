package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.modules.ReadBookModule
import ru.mamykin.core.di.scope.ActivityScope

@ActivityScope
@Subcomponent(modules = [ReadBookModule::class])
interface ReadBookComponent