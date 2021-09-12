package ru.mamykin.foboreader.settings.navigation

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.mamykin.foboreader.settings.presentation.SelectAppLanguageDialogFragment

class SelectAppLanguageScreen : FragmentScreen {

    override fun createFragment(factory: FragmentFactory) = SelectAppLanguageDialogFragment.newInstance()
}