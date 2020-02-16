package ru.mamykin.foboreader.app.navigation

import androidx.navigation.findNavController
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.ui.MainActivity
import ru.mamykin.foboreader.app.ui.TabsFragmentDirections

class NavigatorImpl(
        private val activity: MainActivity
) : Navigator {

    private val navController by lazy { activity.findNavController(R.id.fr_main_nav_host) }

    override fun openMyBooksScreen() {
        activity.openMyBooksScreen()
    }

    override fun openTabsScreen() {
        navController.navigate(R.id.tabsFragment)
    }

    override fun openBook(id: Long) {
        // TODO
    }

    override fun openBookDetails(id: Long) {
        val action = TabsFragmentDirections.actionTabsFragmentToBookDetailsFragment()
                .setBookId(id)

        navController.navigate(action)
    }
}