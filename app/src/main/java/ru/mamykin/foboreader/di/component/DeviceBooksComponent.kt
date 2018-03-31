package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.modules.DeviceBooksModule
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.devicebooks.DeviceBooksFragment

@FragmentScope
@Subcomponent(modules = [DeviceBooksModule::class])
interface DeviceBooksComponent {

    fun inject(fragment: DeviceBooksFragment)
}