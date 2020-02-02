package ru.mamykin.foboreader.platform

import android.app.Activity
import androidx.navigation.findNavController
import ru.mamykin.core.platform.Router
import ru.mamykin.foboreader.R

class MainRouter(
        private val activity: Activity
) : Router {

    override fun openMyBooksScreen() {
        activity.findNavController(R.id.fr_main_nav_host)
                .navigate(R.id.myBooksFragment)
    }
}