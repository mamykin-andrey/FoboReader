package ru.mamykin.settings.di

import dagger.Subcomponent
import ru.mamykin.settings.presentation.SettingsFragment

@Subcomponent
interface SettingsComponent {

    fun inject(fragment: SettingsFragment)
}