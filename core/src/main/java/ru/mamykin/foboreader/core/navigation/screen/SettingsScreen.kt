package ru.mamykin.foboreader.core.navigation.screen

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.mamykin.foboreader.core.navigation.FragmentProvider

class SettingsScreen : FragmentScreen, KoinComponent {

    private val fragmentProvider: FragmentProvider by inject()

    override fun createFragment(factory: FragmentFactory) = fragmentProvider.settingsFragment()
}