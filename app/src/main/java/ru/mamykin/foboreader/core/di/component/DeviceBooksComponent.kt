package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.scope.FragmentScope
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksPresenter

@FragmentScope
@Subcomponent
interface DeviceBooksComponent {

    fun getDeviceBooksPresenter(): DeviceBooksPresenter
}