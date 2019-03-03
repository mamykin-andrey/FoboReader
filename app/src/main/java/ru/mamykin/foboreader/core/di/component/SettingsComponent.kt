package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.scope.ActivityScope
import ru.mamykin.foboreader.presentation.settings.SettingsPresenter

@ActivityScope
@Subcomponent
interface SettingsComponent {

    fun getSettingsPresenter(): SettingsPresenter
}