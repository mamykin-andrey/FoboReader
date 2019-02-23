package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.modules.DeviceBooksModule
import ru.mamykin.foboreader.core.di.scope.FragmentScope
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksFragment

@FragmentScope
@Subcomponent(modules = [DeviceBooksModule::class])
interface DeviceBooksComponent {

    fun inject(fragment: DeviceBooksFragment)
}