package ru.mamykin.foboreader.navigation

import android.app.Activity
import androidx.navigation.findNavController
import ru.mamykin.core.platform.Navigator
import ru.mamykin.foboreader.R

class MainNavigator(
        private val activity: Activity
) : Navigator {

    override fun openMyBooksScreen() {
        activity.findNavController(R.id.fr_main_nav_host)
                .navigate(R.id.myBooksFragment)
    }
}