package ru.mamykin.foboreader.settings.navigation

import androidx.navigation.NavController
import ru.mamykin.foboreader.core.platform.NavControllerHolder
import ru.mamykin.foboreader.settings.presentation.SettingsFragmentDirections

class LocalSettingsNavigator : NavControllerHolder {

    override var navController: NavController? = null

    fun settingsToColorPicker() {
        navController?.navigate(
            SettingsFragmentDirections.settingsToColorPicker()
        )
    }
}