package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.scope.ActivityScope
import ru.mamykin.foboreader.ui.settings.SettingsActivity

@ActivityScope
@Subcomponent
interface SettingsComponent {

    fun inject(activity: SettingsActivity)
}