package ru.mamykin.foboreader.settings.di

import dagger.Subcomponent
import ru.mamykin.foboreader.settings.presentation.ColorPickerDialogFragment
import ru.mamykin.foboreader.settings.presentation.SelectAppLanguageDialog
import ru.mamykin.foboreader.settings.presentation.SettingsFragment

@Subcomponent
interface SettingsComponent {

    fun inject(fragment: SettingsFragment)

    fun inject(fragment: ColorPickerDialogFragment)

    fun inject(fragment: SelectAppLanguageDialog)
}