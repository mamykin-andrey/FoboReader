package ru.mamykin.foboreader.app.navigation

import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.ui.MainActivity
import ru.mamykin.foboreader.app.ui.TabsFragmentDirections
import ru.mamykin.foboreader.core.platform.Navigator

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
        val action = TabsFragmentDirections.tabsToReadBook()
                .setBookId(id)
        navController.navigate(action)
    }

    override fun openBookDetails(id: Long, sharedImage: ImageView) {
        val action = TabsFragmentDirections.tabsToBookDetails()
                .setBookId(id)
        val extras = FragmentNavigatorExtras(sharedImage to id.toString())
        navController.navigate(action, extras)
    }
}