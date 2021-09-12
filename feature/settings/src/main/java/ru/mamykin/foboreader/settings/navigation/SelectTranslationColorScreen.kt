package ru.mamykin.foboreader.settings.navigation

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.mamykin.foboreader.settings.presentation.ColorPickerDialogFragment

class SelectTranslationColorScreen : FragmentScreen {

    override fun createFragment(factory: FragmentFactory) = ColorPickerDialogFragment.newInstance()
}