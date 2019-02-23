package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.scope.ActivityScope
import ru.mamykin.foboreader.presentation.settings.SettingsActivity

@ActivityScope
@Subcomponent
interface SettingsComponent {

    fun inject(activity: SettingsActivity)
}